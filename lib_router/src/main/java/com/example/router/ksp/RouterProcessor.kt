package com.example.router.ksp

import com.example.router.Router
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo
import java.io.OutputStream

class RouterProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    private val routerMap = mutableMapOf<String, String>()
    private var processed = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (processed) return emptyList()
        processed = true

        // 获取目标包名（从配置或默认值）
        val targetPackage = options["router.target_package"] ?: "com.example.router"

        // 获取所有带有 @Router 注解的类
        val symbols = resolver.getSymbolsWithAnnotation(Router::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }

        symbols.forEach { classDeclaration ->
            processRouterClass(classDeclaration)
        }

        if (routerMap.isNotEmpty()) {
            generateRouterRegistry(targetPackage)
        }

        return emptyList()
    }

    private fun processRouterClass(classDeclaration: KSClassDeclaration) {
        val routerAnnotation = classDeclaration.annotations
            .find {
                it.annotationType.resolve()?.declaration?.qualifiedName?.asString() == Router::class.qualifiedName
            } ?: return

        val path = routerAnnotation.arguments
            .firstOrNull { it.name?.asString() == "path" }
            ?.value as? String ?: return

        val qualifiedName = classDeclaration.qualifiedName?.asString() ?: return

        if (routerMap.containsKey(path)) {
            val existing = routerMap[path]
            logger.error("路由路径 '$path' 已存在: $existing", classDeclaration)
        } else {
            routerMap[path] = qualifiedName
        }
    }

    private fun generateRouterRegistry(targetPackage: String) {
        try {
            val fileSpec = FileSpec.builder(targetPackage, "RouterRegistry")
                .addFileComment("AUTO-GENERATED FILE. DO NOT EDIT MANUALLY!\n")
                .addType(
                    TypeSpec.objectBuilder("RouterRegistry")
                        .addProperty(
                            PropertySpec.builder(
                                "pathMap",
                                Map::class.asClassName().parameterizedBy(
                                    String::class.asClassName(),
                                    String::class.asClassName()
                                )
                            )
                                .initializer(buildMapInitializer())
                                .addModifiers(KModifier.PRIVATE)
                                .build()
                        )
                        .addFunction(
                            FunSpec.builder("getDestination")
                                .addParameter("path", String::class)
                                .returns(Class::class.asClassName().parameterizedBy(STAR))
                                .addCode(
                                    """
                                    |return pathMap[path]?.let { className ->
                                    |    try {
                                    |        Class.forName(className)
                                    |    } catch (e: ClassNotFoundException) {
                                    |        throw IllegalStateException("路由目标类未找到: "+className + ":"+ e)
                                    |    }
                                    |}
                                    |""".trimMargin()
                                )
                                .build()
                        )
                        .build()
                )
                .build()

            fileSpec.writeTo(codeGenerator, Dependencies(false))
        } catch (e: Exception) {
            logger.error("生成路由注册表失败: ${e.message}")
        }
    }

    private fun buildMapInitializer(): CodeBlock {
        if (routerMap.isEmpty()) {
            return CodeBlock.of("emptyMap<String, String>()")
        }

        return CodeBlock.builder().apply {
            add("mapOf(\n")
            routerMap.forEach { (path, className) ->
                add("\"$path\" to \"$className\",\n")
            }
            add(")")
        }.build()
    }
}