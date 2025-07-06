package com.cook.booking

import android.app.Application
import android.util.Log
import com.example.common.utils.LogUtils
import dalvik.system.BaseDexClassLoader

class MyApplication : Application(){

    //用于提供全局的 Application
    companion object {
        lateinit var instance: MyApplication // 延迟初始化，在 onCreate() 中赋值
            private set // 私有 set 方法，确保只能在内部设置
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 初始化
        init()
        checkRouterInitialization()
    }

    private fun init() {
        LogUtils.setBaseTag("myLog")
        if (BuildConfig.DEBUG) { // Debug 模式下开启日志和调试功能
            LogUtils.setLogLevel(LogUtils.DEBUG)
        }else{
            LogUtils.setLogLevel(LogUtils.NOTHING)
        }

    }

    private fun checkRouterInitialization() {
        try {
            // 使用当前应用的ClassLoader
            val classLoader = this::class.java.classLoader

            // 尝试加载路由注册表
            val className = "app.access.RouterRegistry"
            Log.d("Router", "尝试加载类: $className")

            // ✅ 使用安全加载方式
            val clazz = classLoader.loadClass(className)
            Log.d("Router", "类加载成功: $clazz")

            // ✅ 尝试调用方法
            val method = clazz.getMethod("getDestination", String::class.java)
            Log.d("Router", "路由方法可用: $method")

            // ✅ 测试一个不存在的路径（应该抛出异常）
            try {
                method.invoke(null, "test.path")
            } catch (e: Exception) {
                Log.d("Router", "测试路径失败（正常）: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e("Router", "路由初始化失败", e)

            // ✅ 打印类路径信息
            printClassLoaderInfo()
        }
    }

    private fun printClassLoaderInfo() {
        val classLoader = this::class.java.classLoader
        Log.d("Router", "ClassLoader: ${classLoader.javaClass.name}")

        // 打印类路径
        try {
            val pathField = BaseDexClassLoader::class.java.getDeclaredField("pathList")
            pathField.isAccessible = true
            val pathList = pathField.get(classLoader)

            val dexElements = pathList.javaClass.getDeclaredField("dexElements")
            dexElements.isAccessible = true
            val elements = dexElements.get(pathList) as Array<*>

            elements.forEachIndexed { i, element ->
                val dexFile = element?.javaClass?.getDeclaredField("dexFile")?.get(element)
                val path = dexFile?.javaClass?.getDeclaredField("mFileName")?.get(dexFile)
                Log.d("Router", "ClassPath[$i]: $path")
            }
        } catch (e: Exception) {
            Log.e("Router", "无法获取ClassLoader路径", e)
        }
    }
}