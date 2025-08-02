package com.rhys.main.home

import android.content.Context
import com.example.common.base.BaseBindFragment
import com.example.common.base.CommonFragmentAdapter
import com.example.common.bean.TabInfo
import com.example.common.constants.RoutePath.PAGE_CHAT
import com.example.common.constants.RoutePath.PAGE_COOK_BOOK
import com.example.common.constants.RoutePath.PAGE_HOME
import com.example.common.constants.RoutePath.PAGE_INDEX
import com.example.common.constants.RoutePath.PAGE_MINE
import com.example.common.constants.RoutePath.PAGE_ORDER
import com.example.common.ext.attach
import com.example.common.ext.visible
import com.example.common.utils.LogUtils
import com.example.common.view.NavigationBarItem
import com.example.common.view.TitleBar
import com.marky.route.annotation.Route
import com.marky.route.api.NRoute
import com.rhys.main.R
import com.rhys.main.databinding.FragmentHomeBinding
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

@Route(path = PAGE_HOME, startPage = true)
class HomeFragment : BaseBindFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    /**
     * 配置tab图标
     */
    private val mTabIcon = listOf(
        R.drawable.selector_navigator_item_passenger_home,
        R.drawable.selector_navigator_item_passenger_way,
        R.drawable.selector_navigator_item_passenger_trip,
        R.drawable.selector_navigator_item_passenger_mine,
        R.drawable.selector_navigator_item_passenger_mine,
    )

    /**
     * 配置viewpager2 tab信息
     */
    private val mTab = listOf(
        TabInfo("首页", "index", NRoute.getFragment(PAGE_INDEX)!!),
        TabInfo("菜谱", "cookbook", NRoute.getFragment(PAGE_COOK_BOOK)!!),
        TabInfo("消息", "chat", NRoute.getFragment(PAGE_CHAT)!!),
        TabInfo("订单", "order", NRoute.getFragment(PAGE_ORDER)!!),
        TabInfo("我的", "mine", NRoute.getFragment(PAGE_MINE)!!)
    )

    override fun initTitleBar(): TitleBar? {
        return null
    }

    override fun initView(binding: FragmentHomeBinding) {

        LogUtils.d("HomeFragment 初始化成功")
        binding!!.indicator.visible()

        binding.vp.isUserInputEnabled = false
        binding.vp.offscreenPageLimit = mTab.size


        val navigator = CommonNavigator(requireContext()).apply {
            adapter = object : CommonNavigatorAdapter() {
                override fun getCount(): Int {
                    return mTab.size
                }

                override fun getTitleView(p0: Context?, p1: Int): IPagerTitleView {
                    val tabInfo = mTab[p1]
                    return NavigationBarItem(
                        context = requireContext(),
                        title = tabInfo.name,
                        resId = mTabIcon[p1]
                    ).apply {
                        setOnClickListener {
                            binding!!.vp.setCurrentItem(p1, false)

                        }
                    }
                }

                override fun getIndicator(p0: Context?): IPagerIndicator? {
                    return null
                }

            }
        }

        navigator.isAdjustMode = true
        binding.indicator.navigator = navigator
        binding.vp.adapter = CommonFragmentAdapter(mTab, this)
        binding.vp.attach(binding.indicator)
    }

    override fun initData(binding: FragmentHomeBinding) {

    }
}