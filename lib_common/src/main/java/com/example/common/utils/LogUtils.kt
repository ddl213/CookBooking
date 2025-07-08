package com.example.common.utils

import android.util.Log
import java.util.concurrent.ConcurrentHashMap

/**
 * 全局日志工具类。
 * 提供了不同级别的日志方法 (v, d, i, w, e)，支持自定义 Tag，
 * 自动获取调用者信息，以及全局日志开关和级别控制。
 */
object LogUtils { // 使用 object 关键字创建单例对象

    // 日志级别常量
    const val VERBOSE = Log.VERBOSE // 2
    const val DEBUG = Log.DEBUG   // 3
    const val INFO = Log.INFO     // 4
    const val WARN = Log.WARN     // 5
    const val ERROR = Log.ERROR   // 6
    const val ASSERT = Log.ASSERT // 7
    const val NOTHING = 8         // 不打印任何日志

    // 默认的日志 Tag 前缀，可外部配置
    @Volatile // 确保多线程环境下可见性
    private var baseTag: String = "myLog"

    // 默认的日志打印级别，生产环境建议设置为 NOTHING 或 ERROR
    @Volatile // 确保多线程环境下可见性
    private var logLevel: Int = DEBUG // 默认为 DEBUG 级别

    // 缓存生成的完整 TAG，Key 是调用堆栈的哈希值
    // 使用 ConcurrentHashMap 更适合多线程环境，且提供线程安全
    private val tagCache: MutableMap<Int, String> = ConcurrentHashMap()

    /**
     * 获取调用者的类名、方法名和行号作为 Tag。
     * 这样可以更准确地定位日志来源。
     * 格式为 "SimpleClassName.methodName(FileName:lineNumber)"
     *
     * 内部会遍历堆栈，跳过 LogUtils 自身的方法。
     * @return 格式化后的调用者信息字符串。
     */
    private fun getCallerInfo(): String {
        val stackTrace = Thread.currentThread().stackTrace
        // 遍历堆栈，找到 LogUtils 外部的第一个调用者
        // 索引 0: getStackTrace
        // 索引 1: getCallerInfo
        // 索引 2: LogUtils.v/d/i/w/e (基础方法) 或 generateFinalTag
        // 索引 3: 调用 LogUtils.v/d/i/w/e 的方法
        val stackDepthOffset = 4 // 调整为4，确保跳过LogUtils的方法，找到实际业务调用者

        for (i in stackDepthOffset until stackTrace.size) {
            val element = stackTrace[i]
            val className = element.className
            // 排除 LogUtils 自身、Java 和 Android 系统的内部类
            if (className != LogUtils::class.java.name &&
                !className.startsWith("java.") &&
                !className.startsWith("android.") &&
                !className.startsWith("dalvik.") &&
                !className.startsWith("androidx.")
            ) {
                val simpleClassName = getSimpleClassName(className)
                return String.format(
                    "%s.%s(%s:%d)",
                    simpleClassName,
                    element.methodName,
                    element.fileName,
                    element.lineNumber
                )
            }
        }
        return "UnknownCaller"
    }

    /**
     * 从完整的类名中提取不带包名和内部类数字的简单类名。
     * 例如：com.example.MyClass$InnerClass1 -> MyClass
     * @param fullClassName 完整的类名字符串。
     * @return 简单类名字符串。
     */
    private fun getSimpleClassName(fullClassName: String): String {
        val lastDot = fullClassName.lastIndexOf('.')
        var simpleName = if (lastDot == -1) fullClassName else fullClassName.substring(lastDot + 1)
        // 处理内部类，移除 $ 后面的数字或匿名内部类标识
        val dollar = simpleName.indexOf('$')
        if (dollar != -1) {
            simpleName = simpleName.substring(0, dollar)
        }
        return simpleName
    }

    /**
     * 生成最终用于 Logcat 的 Tag。
     * 如果提供了 customTag 且不为空，则优先使用 customTag；
     * 否则使用 baseTag 结合调用者信息。
     *
     * 内部会进行缓存以提高性能。
     *
     * @param customTag 用户自定义的 Tag，可为 null 或空字符串。
     * @return 最终的 Tag 字符串。
     */
    private fun generateFinalTag(customTag: String?): String {
        if (!customTag.isNullOrBlank()) {
            return customTag // 如果有自定义 Tag 且不为空，直接使用
        }

        // 使用调用者信息的哈希值作为缓存 Key，确保每次调用上下文一致时返回相同的 Tag
        val callerInfo = getCallerInfo()
        val callerInfoHashCode = callerInfo.hashCode()

        return tagCache.computeIfAbsent(callerInfoHashCode) {
            "$baseTag:$callerInfo" // 如果缓存中没有，则计算并存入
        }
    }


    // Verbose
    fun v(format: String, vararg args: Any?) {
        if (logLevel <= VERBOSE) Log.v(generateFinalTag(null), format.format(*args))
    }
    fun v(msg: String,tag: String? = null ) {
        if (logLevel <= VERBOSE) Log.v(generateFinalTag(tag), msg)
    }

    // Debug
    fun d(msg: String,tag: String? = null) {
        if (logLevel <= DEBUG) Log.d(generateFinalTag(tag), msg)
    }
    fun d(format: String, vararg args: Any?) {
        if (logLevel <= DEBUG) Log.d(generateFinalTag(null), format.format(*args))
    }

    // Info
    fun i(msg: String,tag: String? = null) {
        if (logLevel <= INFO) Log.i(generateFinalTag(tag), msg)
    }
    fun i(format: String, vararg args: Any?) {
        if (logLevel <= INFO) Log.i(generateFinalTag(null), format.format(*args))
    }

    // Warn
    fun w(msg: String,tag: String? = null) {
        if (logLevel <= WARN) Log.w(generateFinalTag(tag), msg)
    }
    fun w(format: String, vararg args: Any?) {
        if (logLevel <= WARN) Log.w(generateFinalTag(null), format.format(*args))
    }

    // Error
    fun e(tr: Throwable?) {
        if (logLevel <= ERROR) Log.e(generateFinalTag(null), "", tr)
    }
    fun e(format: String, vararg args: Any?) {
        if (logLevel <= ERROR) Log.e(generateFinalTag(null), format.format(*args))
    }
    fun e(msg: String, tag: String? = null, tr: Throwable? = null) {
        if (logLevel <= ERROR) Log.e(generateFinalTag(tag), msg, tr)
    }

    /**
     * 设置全局的日志基础 Tag 前缀。
     * @param newTag 新的 Tag 前缀。
     */
    fun setBaseTag(newTag: String) {
        if (newTag.isNotBlank()) {
            this.baseTag = newTag
            clearCache() // Tag 规则改变，清空缓存
        }
    }

    /**
     * 设置日志的打印级别。低于此级别的日志将不会被打印。
     * 例如，设置为 LogUtils.ERROR 则只打印 ERROR 及以上（ASSERT）级别的日志。
     * @param level 日志级别常量 (VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, NOTHING)。
     */
    fun setLogLevel(level: Int) {
        if (level in VERBOSE..NOTHING) {
            this.logLevel = level
            Log.d(baseTag, "日志级别已设置为: ${getLevelName(level)}")
        } else {
            Log.e(baseTag, "设置的日志级别无效: $level")
        }
    }

    /**
     * 获取日志级别名称，用于日志输出。
     * @param level 日志级别整数值。
     * @return 对应的日志级别名称字符串。
     */
    private fun getLevelName(level: Int): String {
        return when (level) {
            VERBOSE -> "VERBOSE"
            DEBUG -> "DEBUG"
            INFO -> "INFO"
            WARN -> "WARN"
            ERROR -> "ERROR"
            ASSERT -> "ASSERT"
            NOTHING -> "NOTHING"
            else -> "UNKNOWN"
        }
    }

    /**
     * 清空 Tag 缓存。在设置新的 baseTag 或 logLevel 后，建议清空缓存。
     */
    fun clearCache() {
        tagCache.clear()
    }
}