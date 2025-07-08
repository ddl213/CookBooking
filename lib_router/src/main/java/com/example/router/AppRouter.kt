package com.example.router

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.*
/**
 * 路由核心类，整合路由初始化、导航和参数获取功能
 */
class AppRouter private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var instance: AppRouter? = null

        fun init(context: Context, navController: NavController? = null) {
            instance = AppRouter(context).apply {
                this.navController = navController
                registerModuleRoutes()
            }
        }

        fun get(): NavigationBuilder {
            val router =  instance?: throw IllegalStateException("Router not initialized. Call init() first.")
            return router.NavigationBuilder(router)
        }

        fun <T> getArgument(key: String, bundle: Bundle?, default: T): T {
            return bundle?.run {
                when (default) {
                    is String -> getString(key, default) as T
                    is Int -> getInt(key, default) as T
                    is Boolean -> getBoolean(key, default) as T
                    is Float -> getFloat(key, default) as T
                    is Double -> getDouble(key, default) as T
                    else -> get(key) as? T ?: default
                }
            } ?: default
        }
    }

    private val routeMap = mutableMapOf<String, String>()
    private var navController: NavController? = null

    /**
     * 修复点：动态构建注册类名
     */
    private fun registerModuleRoutes() {
        try {
            val packageName = context.packageName
            // 从配置获取模块名或使用默认
            val moduleName = "app" // 实际应从配置获取

            // 动态构建注册类名
            val registryClass = Class.forName("$packageName.generated.${moduleName}RouteRegistry")

            // 使用正确的参数类型
            val method = registryClass.getMethod("registerRoutes", AppRouter::class.java)
            method.invoke(null, this)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to register module routes", e)
        }
    }

    fun registerRoute(path: String, className: String) {
        if (routeMap.containsKey(path)) {
            throw IllegalArgumentException("Route conflict: path '$path' already registered")
        }
        routeMap[path] = className
    }

    fun navigate(): NavigationBuilder {
        return NavigationBuilder(this)
    }

    internal fun getClassName(path: String): String {
        return routeMap[path] ?: throw IllegalArgumentException("Route not found: $path")
    }



    /**
     * 导航构建器
     */
    inner class NavigationBuilder(private val router: AppRouter) {
        private var path: String = ""
        private var args: Bundle? = null
        private var navOptions: NavOptions? = null
        private var closeSelf: Boolean = false
        private var asActivity: Boolean = false

        /**
         * 设置目标路径
         */
        fun to(path: String): NavigationBuilder {
            this.path = path
            return this
        }

        /**
         * 添加参数
         */
        fun withArguments(bundle: Bundle): NavigationBuilder {
            this.args = bundle
            return this
        }

        /**
         * 添加参数
         */
        fun withArguments(vararg pairs: Pair<String, Any?>): NavigationBuilder {
            this.args = Bundle().apply {
                pairs.forEach { (key, value) ->
                    when (value) {
                        is Int -> putInt(key, value)
                        is String -> putString(key, value)
                        is Boolean -> putBoolean(key, value)
                        is Float -> putFloat(key, value)
                        is Double -> putDouble(key, value)
                        is Bundle -> putBundle(key, value)
                        else -> putString(key, value.toString())
                    }
                }
            }
            return this
        }

        /**
         * 设置导航选项
         */
        fun withOptions(options: NavOptions): NavigationBuilder {
            this.navOptions = options
            return this
        }

        /**
         * 导航后关闭当前页面
         */
        fun closeSelf(): NavigationBuilder {
            this.closeSelf = true
            return this
        }

        /**
         * 指定为目标Activity
         */
        fun toActivity(): NavigationBuilder {
            this.asActivity = true
            return this
        }

        /**
         * 指定为目标Fragment
         */
        fun toFragment(): NavigationBuilder {
            this.asActivity = false
            return this
        }

        /**
         * 执行导航
         */
        fun navigate() {
            if (asActivity) {
                navigateAsActivity()
            } else {
                navigateAsFragment()
            }
        }

        private fun navigateAsFragment() {
            val controller = router.navController ?: throw IllegalStateException("NavController not initialized")
            val deepLink = buildDeepLink()
            val finalOptions = buildNavOptions()
            controller.navigate(Uri.parse(deepLink), finalOptions)
        }

        private fun navigateAsActivity() {
            val context = router.context
            val className = router.getClassName(path)
            val intent = Intent().apply {
                setClassName(context, className)
                data = Uri.parse(buildDeepLink())
                args?.let { putExtras(it) }
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        private fun buildDeepLink(): String {
            val builder = Uri.parse(path).buildUpon()
            args?.keySet()?.forEach { key ->
                args?.get(key)?.toString()?.let { value ->
                    builder.appendQueryParameter(key, value)
                }
            }
            return builder.build().toString()
        }

        private fun buildNavOptions(): NavOptions? {
            if (!closeSelf) return navOptions
            val currentRoute = router.navController?.currentDestination?.route ?: return navOptions
            return NavOptions.Builder()
                .setPopUpTo(currentRoute, true)
                .build()
        }
    }
}


/**
 * 参数获取扩展
 */
// 从Bundle中获取参数
fun <T> Bundle?.getRouteArg(key: String, default: T): T {
    return AppRouter.getArgument(key, this, default)
}

// 从Intent中获取参数
fun <T> Intent.getRouteArg(key: String, default: T): T {
    return AppRouter.getArgument(key, extras, default)
}
