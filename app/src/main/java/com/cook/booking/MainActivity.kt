package com.cook.booking

import android.util.Log
import androidx.navigation.findNavController
import com.example.annotations.Route
import com.example.common.base.BaseBindActivity
import com.example.common.constants.RoutePath.PATH_MAIN
import com.cook.booking.databinding.ActivityMainBinding
import com.example.common.constants.Constants
import com.example.common.constants.RoutePath.PATH_SPLASH
import com.example.common.utils.LogUtils
import com.example.common.utils.MMKVUtils
import com.example.router.AppRouter

@Route(path = PATH_MAIN)
class MainActivity : BaseBindActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val SPLASH_SUPPRESSION_TIME_MS = 0
    override fun initView(binding: ActivityMainBinding) {
        LogUtils.d(msg = "当前在MainActivity")
        val navController = findNavController(R.id.nav_host_fragment)
        AppRouter.init( this, navController)
        showSplash()
    }

    override fun initData(binding: ActivityMainBinding) {

    }

    override fun initListener(binding: ActivityMainBinding) {

    }

    private fun showSplash(){
        AppRouter.get()
            .to(PATH_SPLASH)
            .toFragment()
            .navigate()


        val lastExitTime = MMKVUtils.getLong(Constants.KEY_LAST_EXIT_TIME)
        val currentTime = System.currentTimeMillis()
        LogUtils.d(msg = "Current time: $currentTime, Last exit time: $lastExitTime")

        if (lastExitTime == 0L || (currentTime - lastExitTime) > SPLASH_SUPPRESSION_TIME_MS) {
            // 首次启动或超过指定时间，显示 SplashFragment
            LogUtils.d(msg =  "Displaying SplashFragment.")
        }
    }

}