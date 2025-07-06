package com.example.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.findNavController

class AppRouter private constructor(
    private val navigationSource: Any,
    private val deepLink: Uri? = null,
    private val args: Bundle? = null,
    private val navOptions: NavOptions? = null,
    private val navigatorExtras: Navigator.Extras? = null
) {
    class Builder(private val source: Any) {
        private var deepLink: Uri? = null
        private var args: Bundle? = null
        private var navOptions: NavOptions? = null
        private var navigatorExtras: Navigator.Extras? = null

        fun to(path: String) = apply {
            this.deepLink = Uri.parse(path)
        }

        fun withArguments(vararg pairs: Pair<String, Any?>) = apply {
            this.args = bundleOf(*pairs)
        }

        fun withArguments(bundle: Bundle) = apply {
            this.args = bundle
        }

        fun withOptions(options: NavOptions) = apply {
            this.navOptions = options
        }

        fun withExtras(extras: Navigator.Extras) = apply {
            this.navigatorExtras = extras
        }

        fun build(): AppRouter {
            return AppRouter(
                navigationSource = source,
                deepLink = deepLink,
                args = args,
                navOptions = navOptions,
                navigatorExtras = navigatorExtras
            )
        }

        fun navigate() = build().navigate()
    }

    companion object {
        fun from(context: Context) = Builder(context)
        fun from(fragment: Fragment) = Builder(fragment)

        @JvmStatic
        fun <T> getArgument(source: Any, key: String, defaultValue: T? = null): T? {
            return when (source) {
                is Fragment -> source.arguments?.get(key) as? T ?: defaultValue
                is Activity -> source.intent?.extras?.get(key) as? T ?: defaultValue
                else -> defaultValue
            }
        }
    }

    fun navigate() {
        when (navigationSource) {
            is NavController -> navigateWithNavController(navigationSource)
            is Fragment -> navigateWithFragment(navigationSource)
            is Context -> navigateWithContext(navigationSource)
            else -> throw IllegalArgumentException("不支持的导航源: ${navigationSource::class.java}")
        }
    }

    private fun navigateWithNavController(navController: NavController) {
        requireNotNull(deepLink) { "DeepLink must be set for NavController navigation" }
        val finalArgs = args ?: Bundle()

        // 创建包含所有参数的 URI
        val uriWithParams = deepLink.buildUpon().apply {
            for (key in finalArgs.keySet()) {
                finalArgs.get(key)?.let { value ->
                    when (value) {
                        is String -> appendQueryParameter(key, value)
                        is Int -> appendQueryParameter(key, value.toString())
                        is Long -> appendQueryParameter(key, value.toString())
                        is Float -> appendQueryParameter(key, value.toString())
                        is Double -> appendQueryParameter(key, value.toString())
                        is Boolean -> appendQueryParameter(key, value.toString())
                        else -> Log.w("AppRouter", "不支持的类型: ${value::class.java}, 键: $key")
                    }
                }
            }
        }.build()

        // 创建导航请求
        val request = NavDeepLinkRequest.Builder
            .fromUri(uriWithParams)
            .build()

        // 执行导航
        navController.navigate(request, navOptions, navigatorExtras)
    }

    private fun navigateWithFragment(fragment: Fragment) {
        try {
            val navController = fragment.findNavController()
            navigateWithNavController(navController)
        } catch (e: IllegalStateException) {
            navigateWithContext(fragment.requireContext())
        }
    }

    private fun navigateWithContext(context: Context) {
        requireNotNull(deepLink) { "Context导航必须设置DeepLink" }

        val path = deepLink.toString()

        try {
            // 直接访问全局路由注册表
            val destination = getDestinationForPath(path)
                ?: throw IllegalArgumentException("找不到目标路径: $path")

            Intent(context, destination).apply {
                data = deepLink
                args?.let { putExtras(it) }

                if (context !is Activity) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                context.startActivity(this)
            }
        } catch (e: Exception) {
            Log.e("AppRouter", "导航失败: ${e.message}")
            navigateToErrorPage(context, e)
        }
    }

    private fun getDestinationForPath(path: String): Class<*>? {
        return try {
            // 访问全局路由注册表
            val registryClass = Class.forName("com.example.cookbooking.access.RouterRegistry")
            val method = registryClass.getMethod("getDestination", String::class.java)
            method.invoke(null, path) as Class<*>
        } catch (e: Exception) {
            Log.e("AppRouter", "获取目标失败: ${e.message}")
            null
        }
    }

    private fun navigateToErrorPage(context: Context, error: Throwable) {
        try {
            val errorIntent = Intent(context, Class.forName("com.example.cookbooking.ErrorActivity"))
            errorIntent.putExtra("error_message", error.message)
            errorIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(errorIntent)
        } catch (e: Exception) {
            Log.e("AppRouter", "无法导航到错误页面: ${e.message}")
        }
    }
}
