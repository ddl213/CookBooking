package com.example.cookbooking

import android.app.Application
import com.example.common.utils.LogUtils

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
    }

    private fun init() {
        LogUtils.setBaseTag("myLog")
        if (BuildConfig.DEBUG) { // Debug 模式下开启日志和调试功能
            LogUtils.setLogLevel(LogUtils.DEBUG)
        }else{
            LogUtils.setLogLevel(LogUtils.NOTHING)
        }

        //初始化生命周期监听
        MyActivityLifecycle.init(this)
    }
}