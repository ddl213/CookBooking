package com.example.router

/**
 * 临时占位类，确保 AppRouter 能编译通过
 *
 * 实际运行时会被 KSP 生成的 RouterRegistry 替换
 */
object RouterRegistry {
    private val pathMap: Map<String, String> = mapOf(
        // 这里的内容会在编译时由KSP自动填充
        // 格式: "路由路径" to "完整类名"
    )

    @Throws(ClassNotFoundException::class)
    fun getDestination(path: String): Class<*> {
        val className = pathMap[path]
            ?: throw ClassNotFoundException("未注册的路由路径: $path")

        return try {
            Class.forName(className)
        } catch (e: ClassNotFoundException) {
            throw IllegalStateException("路由目标类未找到: $className", e)
        }
    }
}