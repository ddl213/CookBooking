package com.cook.booking.splash

import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath.PATH_HOME
import com.example.common.constants.RoutePath.PATH_SPLASH
import com.example.common.utils.LogUtils
import com.cook.booking.databinding.FragmentSplashBinding

//@Router(PATH_SPLASH)
class SplashFragment : BaseBindFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    override fun initView(binding: FragmentSplashBinding) {
        LogUtils.d(msg = "跳转PATH_HOME前")

//        AppRouter.from(this)
//            .to(PATH_HOME)
//            .navigate()
    }

    override fun initData(binding: FragmentSplashBinding) {

    }

    override fun initListener(binding: FragmentSplashBinding) {

    }

    override fun onViewDestroy() {

    }
}