package com.cook.booking

import android.app.Application
import com.example.common.utils.LogUtils

class MyApplication : Application(){

    //用于提供全局的 Application
    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        LogUtils.d("初始化成功")
    }

}