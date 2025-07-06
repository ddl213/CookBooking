package com.example.cookbooking

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.common.constants.Constants.KEY_LAST_EXIT_TIME
import com.example.common.utils.LogUtils
import com.example.common.utils.MMKVUtils

object MyActivityLifecycle : Application.ActivityLifecycleCallbacks { // 实现 Application.ActivityLifecycleCallbacks 接口

    private val TAG = "ActivityLifecycle"

    private var activityCount = 0 // 记录当前处于前台（Started 或 Resumed）的 Activity 数量

    /**
     * 初始化 AppExitTracker。
     * 应该在 MyApplication 的 onCreate() 中调用，并将自身注册为 Activity 生命周期回调。
     * @param application Application 实例。
     */
    fun init(application: Application) {
        // 注册 Activity 生命周期回调
        application.registerActivityLifecycleCallbacks(this)
        LogUtils.d(tag = TAG, "AppExitTracker initialized and registered as ActivityLifecycleCallbacks.")
    }

    /**
     * 获取上次应用进入后台的时间戳。
     * @return 上次退出时间戳，如果未记录则返回0L。
     */
    fun getLastExitTime(): Long {
        return MMKVUtils.getLong(KEY_LAST_EXIT_TIME, 0L)
    }

    /**
     * 保存当前时间作为上次应用进入后台的时间戳。
     * @param time 时间戳。
     */
    private fun saveLastExitTime(time: Long) {
        MMKVUtils.setLong(KEY_LAST_EXIT_TIME, time)
    }

    // --- ActivityLifecycleCallbacks 接口实现 ---
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        LogUtils.d(tag = TAG, "onActivityCreated: ${activity.javaClass.simpleName}")
    }

    override fun onActivityStarted(activity: Activity) {
        LogUtils.d(tag = TAG, "onActivityStarted: ${activity.javaClass.simpleName}")
        activityCount++
        // 当有 Activity 启动时，如果之前所有 Activity 都已停止，说明应用从后台回到前台
        if (activityCount == 1) {
            LogUtils.d(tag = TAG, "App entered foreground.")
            // 可以在这里清除上次退出时间，如果开屏逻辑只在“冷启动”或“长时间后台”后显示
            // clearLastExitTime() // 根据业务需求决定是否清除
        }
    }

    override fun onActivityResumed(activity: Activity) {
        LogUtils.d(tag = TAG, "onActivityResumed: ${activity.javaClass.simpleName}")
    }

    override fun onActivityPaused(activity: Activity) {
        LogUtils.d(tag = TAG, "onActivityPaused: ${activity.javaClass.simpleName}")
    }

    override fun onActivityStopped(activity: Activity) {
        LogUtils.d(tag = TAG, "onActivityStopped: ${activity.javaClass.simpleName}")
        activityCount--
        // 当所有 Activity 都停止时，表示应用进入后台
        if (activityCount == 0) {
            val currentTime = System.currentTimeMillis()
            saveLastExitTime(currentTime)
            LogUtils.d(tag = TAG, "App entered background. Last exit time saved: $currentTime")
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        LogUtils.d(tag = TAG, "onActivitySaveInstanceState: ${activity.javaClass.simpleName}")
    }

    override fun onActivityDestroyed(activity: Activity) {
        LogUtils.d(tag = TAG, "onActivityDestroyed: ${activity.javaClass.simpleName}")
    }
}