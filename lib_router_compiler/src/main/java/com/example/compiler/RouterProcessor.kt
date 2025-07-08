package com.example.compiler

import com.example.annotations.Route
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ksp.writeTo

class RouterProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    private val routeMap = mutableMapOf<String, String>()
    private var processed = false
    private val ROUTE_ANNOTATION_NAME = Route::class.qualifiedName!!

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (processed) return emptyList()
        processed = true

        val moduleName = options["MODULE_NAME"] ?: "unknown"
        logger.info("RouterProcessor started for module: $moduleName")

        val symbols = resolver.getSymbolsWithAnnotation(ROUTE_ANNOTATION_NAME)
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .toList()

        logger.info("Found ${symbols.size} @Route annotated classes")

        symbols.forEach { classDeclaration ->
            processRouteClass(classDeclaration)
        }

        if (routeMap.isNotEmpty()) {
            generateRouterRegistry(moduleName)
        }

        return emptyList()
    }

    private fun processRouteClass(classDeclaration: KSClassDeclaration) {
        var className = classDeclaration.qualifiedName?.asString() ?: run {
            logger.error("Class has no qualified name: ${classDeclaration.simpleName.asString()}")
            return
        }

        // 修复点：移除 Kotlin 类后缀
        if (className.endsWith("Kt")) {
            className = className.removeSuffix("Kt")
        }

        val routeAnnotation = classDeclaration.annotations.firstOrNull {
            it.annotationType.resolve()?.declaration?.qualifiedName?.asString() == ROUTE_ANNOTATION_NAME
        } ?: run {
            logger.error("@Route annotation not found on class: $className")
            return
        }

        val path = routeAnnotation.arguments.firstOrNull { it.name?.asString() == "path" }
            ?.value as? String ?: run {
            logger.error("Missing 'path' argument in @Route annotation for class: $className")
            return
        }

        if (routeMap.containsKey(path)) {
            logger.error("Duplicate route path: '$path' already mapped to ${routeMap[path]}")
            return
        }

        routeMap[path] = className
        logger.info("Mapped route: '$path' -> $className")
    }

    private fun generateRouterRegistry(moduleName: String) {
        try {
            val packageName = options["ROUTER_PACKAGE"] ?: "com.example.router.generated"
            logger.info("Generating RouterRegistry in package: $packageName")

            // 修复点：使用正确的 AppRouter 类型
            val appRouterType = ClassName("com.example.router", "AppRouter")

            val fileSpec = FileSpec.builder(packageName, "${moduleName}RouteRegistry")
                .addFileComment("Auto-generated router registry for $moduleName module. Do not edit!")
                .addType(
                    TypeSpec.objectBuilder("${moduleName}RouteRegistry")
                        .addFunction(
                            FunSpec.builder("registerRoutes")
                                .addParameter("router", appRouterType)
                                .addCode(buildRegistrationCode())
                                .build()
                        )
                        .build()
                )
                .build()

            fileSpec.writeTo(codeGenerator, Dependencies(aggregating = true))
            logger.info("Route registry generated for $moduleName module")
        } catch (e: Exception) {
            logger.exception(e)
        }
    }

    private fun buildRegistrationCode(): CodeBlock {
        return CodeBlock.builder().apply {
            add("router.apply {\n")
            routeMap.forEach { (path, className) ->
                add("""registerRoute("$path", "$className")\n""")
            }
            add("}")
        }.build()
    }
}
