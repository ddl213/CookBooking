package com.example.cookbooking.splash

import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath.PATH_HOME
import com.example.common.constants.RoutePath.PATH_SPLASH
import com.example.cookbooking.databinding.FragmentSplashBinding
import com.example.router.AppRouter
import com.example.router.Router

@Router(path = PATH_SPLASH)
class SplashFragment : BaseBindFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    override fun initView(binding: FragmentSplashBinding) {

        AppRouter.from(this)
            .to(PATH_HOME)
            .navigate()
    }

    override fun initData(binding: FragmentSplashBinding) {

    }

    override fun initListener(binding: FragmentSplashBinding) {

    }

    override fun onViewDestroy() {

    }
}