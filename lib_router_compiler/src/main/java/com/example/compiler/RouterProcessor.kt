package com.example.compiler

import com.example.annotations.Router
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo

class RouterProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    private val routerMap = mutableMapOf<String, String>()
    private var processed = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (processed) {
            logger.info("RouterProcessor: Already processed, returning empty list.")
            return emptyList()
        }
        processed = true

        logger.info("RouterProcessor: 开始处理路由注解")

        // 获取目标包名
        val targetPackage = options["router.target_package"] ?: "com.example.router"
        logger.info("RouterProcessor: 配置的目标包名: $targetPackage")

        // 获取所有带有 @Router 注解的类
        val routerAnnotationQualifiedName = Router::class.qualifiedName!!
        logger.info("RouterProcessor: 查找注解: $routerAnnotationQualifiedName")

        val symbols = resolver.getSymbolsWithAnnotation(routerAnnotationQualifiedName)
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .toList() // 将 Sequence 转换为 List，以便多次迭代和获取大小

        logger.info("RouterProcessor: 发现 ${symbols.size} 个带有 @Router 注解的类。")

        if (symbols.isEmpty()) {
            logger.warn("RouterProcessor: 未发现任何带有 @Router 注解的类，跳过代码生成。")
        }

        symbols.forEach { classDeclaration ->
            logger.info("RouterProcessor: 正在处理类: ${classDeclaration.qualifiedName?.asString()}")
            processRouterClass(classDeclaration)
        }

        if (routerMap.isNotEmpty()) {
            generateRouterRegistry(targetPackage)
        } else {
            logger.info("RouterProcessor: routerMap 为空，不生成 RouterRegistry。")
        }

        return emptyList()
    }

    private fun processRouterClass(classDeclaration: KSClassDeclaration) {
        val routerAnnotation = classDeclaration.annotations
            .find {
                it.annotationType.resolve()?.declaration?.qualifiedName?.asString() == Router::class.qualifiedName
            } ?: run {
            logger.error("RouterProcessor: 未找到 ${Router::class.qualifiedName} 注解在类 ${classDeclaration.qualifiedName?.asString()}", classDeclaration)
            return
        }

        val path = routerAnnotation.arguments
            .firstOrNull { it.name?.asString() == "path" }
            ?.value as? String ?: run {
            logger.error("RouterProcessor: 未找到 'path' 参数在注解 ${Router::class.qualifiedName} 中，类 ${classDeclaration.qualifiedName?.asString()}", classDeclaration)
            return
        }

        val qualifiedName = classDeclaration.qualifiedName?.asString() ?: run {
            logger.error("RouterProcessor: 无法获取类 ${classDeclaration.simpleName.asString()} 的完整限定名", classDeclaration)
            return
        }

        if (routerMap.containsKey(path)) {
            val existing = routerMap[path]
            logger.error("RouterProcessor: 路由路径 '$path' 已存在: $existing (新: $qualifiedName)", classDeclaration)
        } else {
            routerMap[path] = qualifiedName
            logger.info("RouterProcessor: 添加路由: '$path' -> '$qualifiedName'")
        }
    }

    private fun generateRouterRegistry(targetPackage: String) {
        try {
            val finalPackage = targetPackage // 确保这里使用传入的 targetPackage

            logger.info("RouterProcessor: 正在生成路由注册表到包: $finalPackage")

            val fileSpec = FileSpec.builder(finalPackage, "RouterRegistry")
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

            fileSpec.writeTo(
                codeGenerator,
                Dependencies(aggregating = false),
            )

            logger.info("RouterProcessor: 路由注册表生成成功")
        } catch (e: Exception) {
            logger.error("RouterProcessor: 生成路由注册表失败: ${e.message}") // 打印完整异常
        }
    }

    private fun buildMapInitializer(): CodeBlock {
        if (routerMap.isEmpty()) {
            return CodeBlock.of("emptyMap<String, String>()")
        }

        return CodeBlock.builder().apply {
            add("mapOf(\n")
            routerMap.forEach { (path, className) ->
                add("    \"$path\" to \"$className\",\n") // 增加缩进，更美观
            }
            add(")")
        }.build()
    }
}
