package com.rhys.mine

import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath
import com.example.common.utils.LogUtils
import com.example.common.view.TitleBar
import com.marky.route.annotation.Route
import com.rhys.mine.databinding.MineFragmentMineBinding

@Route(RoutePath.PAGE_MINE)
class MineFragment : BaseBindFragment<MineFragmentMineBinding>(MineFragmentMineBinding::inflate) {


    override fun initTitleBar(): TitleBar {
        return binding.titleBar
    }

    override fun initView(binding: MineFragmentMineBinding) {
        LogUtils.d("MineFragment 初始化成功")
    }

    override fun initData(binding: MineFragmentMineBinding) {

    }
}