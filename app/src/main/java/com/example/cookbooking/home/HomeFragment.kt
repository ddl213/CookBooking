package com.example.cookbooking.home

import android.content.Intent
import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath.PATH_HOME
import com.example.common.utils.LogUtils
import com.example.cookbooking.databinding.FragmentHomeBinding
import com.example.router.Router

@Router(path = PATH_HOME)
class HomeFragment : BaseBindFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun initView(binding: FragmentHomeBinding) {
        setupViewPagerAndTabs()

    }


    override fun initData(binding: FragmentHomeBinding) {}

    override fun initListener(binding: FragmentHomeBinding) {}

    override fun onViewDestroy() {}

    /**
     * 配置 ViewPager2 和 TabLayout。
     */
    private fun setupViewPagerAndTabs() {
        // 创建 ViewPager2 的适配器
        // HomePagerAdapter 需要一个 Fragment 实例作为参数，这里传入 HomeFragment 自身
//        val pagerAdapter = HomePagerAdapter(this)
//
//        binding.viewPager.adapter = pagerAdapter
//        // 设置 ViewPager2 的离屏页面限制。
//        // 适当增加可以减少 Fragment 的销毁和重建，但会增加内存消耗。
//        // 对于数据持久化，ViewModel 才是关键，它在 Fragment 销毁重建后依然存在。
//        binding.viewPager.offscreenPageLimit = 2 // 例如，保留当前页左右各2页
//
//        // 将 TabLayout 与 ViewPager2 关联起来
//        // TabLayoutMediator 会根据 ViewPager2 的适配器自动创建 Tab
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = when (position) {
//                0 -> "首页"      // 对应 IndexFragment
//                1 -> "仪表盘"    // 对应 DashboardFragment
//                2 -> "我的"      // 对应 ProfileFragment
//                else -> "未知"   // 默认情况
//            }
//        }.attach() // 必须调用 attach() 来完成关联
//
//        // 默认选中第一个 Tab (首页)
//        binding.viewPager.currentItem = 0
    }

    /**
     * 处理来自宿主 Activity (SplashActivity) 的新 Intent。
     * 当通过 FLAG_ACTIVITY_CLEAR_TOP 导航回 SplashActivity 时，此方法会被调用。
     * - 根据 Intent 中的 "target_tab_index" 参数切换 ViewPager 页面。
     * - 只有当 Intent 包含额外数据时，才将 Intent 传递给当前显示的子 Fragment 进行数据刷新。
     * @param intent 新的 Intent 对象，可能包含导航参数或返回数据。
     */
    fun handleNewIntent(intent: Intent?) {
        LogUtils.d(msg =  "HomeFragment received new Intent: $intent")

        // 1. 处理 ViewPager 页面切换
        // 从 Intent 中获取目标 Tab 的索引，如果不存在则为 -1
        val targetTabIndex = intent?.getIntExtra("target_tab_index", -1)
        // 检查索引是否有效且在 ViewPager 的范围内
        // binding.viewPager.adapter?.itemCount ?: 0 确保适配器和 itemCount 非空
//        if (targetTabIndex != -1 && targetTabIndex < (binding.viewPager.adapter?.itemCount ?: 0)) {
//            binding.viewPager.currentItem = targetTabIndex // 切换 ViewPager 页面
//            LogUtils.d(msg =  "ViewPager switched to tab index: $targetTabIndex")
//        }

        // 2. 根据 Intent 是否包含额外数据，决定是否通知子 Fragment 刷新
        // intent?.extras?.isEmpty() ?: true 检查 Intent 的 extras 是否为 null 或为空
        // 如果 extras 不为空 (即 Intent 携带了数据)，则通知子 Fragment
        if (!(intent?.extras?.isEmpty() ?: true)) {
            LogUtils.d(msg =  "Intent contains data. Notifying current child fragment for refresh.")
            // 获取当前 ViewPager2 显示的 Fragment
            // ViewPager2 的 Fragment 标签通常是 "f" + position，可以通过 childFragmentManager.findFragmentByTag 获取
//            val currentFragment = childFragmentManager.findFragmentByTag("f" + binding.viewPager.currentItem)
//            // 检查当前 Fragment 是否是 IndexFragment，并调用其刷新方法
//            if (currentFragment is IndexFragment) { // 假设 IndexFragment 是唯一需要处理刷新数据的 Fragment
//                currentFragment.refreshDataFromNewIntent(intent)
//            }
            // 如果有其他 Fragment 类型也需要处理 Intent 数据，可以在这里添加相应的判断和调用
            // else if (currentFragment is AnotherFragment) {
            //     (currentFragment as AnotherFragment).refreshDataFromNewIntent(intent)
            // }
        } else {
            // Intent 没有额外数据，只处理了页面跳转（如果指定了 target_tab_index）
            LogUtils.d(msg =  "Intent has no extra data. Only handling page jump if specified.")
        }
    }
}