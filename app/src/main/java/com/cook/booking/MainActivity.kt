package com.cook.booking

import com.example.annotations.Router
import com.example.common.base.BaseBindActivity
import com.example.common.constants.RoutePath.PATH_HOME
import com.example.common.constants.RoutePath.PATH_MAIN
import com.cook.booking.databinding.ActivityMainBinding
import com.example.common.utils.LogUtils
import com.example.router.AppRouter

@Router(path = PATH_MAIN)
class MainActivity : BaseBindActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun initView(binding: ActivityMainBinding) {
        //showSplash()
        LogUtils.d(msg = "跳转PATH_HOME前")
        AppRouter.from(this)
            .to(PATH_HOME)
            .navigate()
        LogUtils.d(msg = "跳转到PATH_HOME")
    }

    override fun initData(binding: ActivityMainBinding) {

    }

    override fun initListener(binding: ActivityMainBinding) {

    }


    /**
     * 处理 Activity 的新 Intent。
     * 当通过 FLAG_ACTIVITY_CLEAR_TOP 导航到此 Activity 时，会调用此方法。
     * 可以用来处理从其他页面返回首页时传递的参数。
     */
//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        // 更新 Activity 的 Intent
//        setIntent(intent)
//        LogUtils.d(msg = "onNewIntent called. Intent: $intent")
//        handleInitialNavigation(intent)
//    }

//    /**
//     * 集中处理首次启动或从后台返回时的导航逻辑。
//     * @param intent 启动 Activity 的 Intent。
//     */
//    private fun handleInitialNavigation(intent: Intent?) {
//        // 检查是否是从 ARoute 带 showHome 标志跳转过来的
//        val showHomeDirectly = intent?.getBooleanExtra("showHome", false) ?: false
//
//        if (showHomeDirectly) {
//            LogUtils.d(msg =  "Directly showing HomeFragment via ARoute flag.")
//            showHomeFragment()
//            // 传递 Intent 给 HomeFragment 处理，可能包含返回参数
//            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//            if (currentFragment is HomeFragment) {
//                currentFragment.handleNewIntent(intent)
//            }
//        } else {
//            // 原始的开屏逻辑
//            val lastExitTime = MyActivityLifecycle.getLastExitTime()
//            val currentTime = System.currentTimeMillis()
//
//            LogUtils.d(msg = "Current time: $currentTime, Last exit time: $lastExitTime")
//
//            if (lastExitTime == 0L || (currentTime - lastExitTime) > SPLASH_SUPPRESSION_TIME_MS) {
//                // 首次启动或超过指定时间，显示 SplashFragment
//                LogUtils.d(msg = "Displaying SplashFragment.")
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, SplashFragment())
//                    .commit()
//            } else {
//                // 在指定时间内返回，直接跳转到 HomeFragment
//                LogUtils.d(msg = "Skipping SplashFragment, directly to HomeFragment.")
//                showHomeFragment()
//            }
//        }
//    }
//
//    private fun showSplash(){
//        val lastExitTime = MMKVUtils.getLong(Constants.KEY_LAST_EXIT_TIME)
//        val currentTime = System.currentTimeMillis()
//
//        LogUtils.d(msg = "Current time: $currentTime, Last exit time: $lastExitTime")
//
//        if (lastExitTime == 0L || (currentTime - lastExitTime) > SPLASH_SUPPRESSION_TIME_MS) {
//            // 首次启动或超过指定时间，显示 SplashFragment
//            LogUtils.d(msg =  "Displaying SplashFragment.")
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, SplashFragment())
//                .commit()
//        } else {
//            // 在指定时间内返回，直接跳转到 HomeFragment
//            LogUtils.d(msg =  "Skipping SplashFragment, directly to HomeFragment.")
//            showHomeFragment()
//
//        }
//    }
//
//    /**
//     * 统一显示 HomeFragment 的方法。
//     * 避免重复代码。
//     */
//    private fun showHomeFragment() {
//        // 只有当当前显示的不是 HomeFragment 时才进行替换，避免不必要的 Fragment 事务
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
//        if (currentFragment !is HomeFragment) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, HomeFragment())
//                .commitAllowingStateLoss()
//            LogUtils.d(msg =  "HomeFragment replaced into container.")
//        } else {
//            LogUtils.d(msg =  "HomeFragment already active.")
//        }
//    }
}