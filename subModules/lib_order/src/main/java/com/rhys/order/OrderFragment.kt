package com.rhys.order

import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath
import com.example.common.utils.LogUtils
import com.example.common.view.TitleBar
import com.marky.route.annotation.Route
import com.rhys.order.databinding.OrderFragmentOrderBinding

@Route(RoutePath.PAGE_ORDER)
class OrderFragment : BaseBindFragment<OrderFragmentOrderBinding>(OrderFragmentOrderBinding::inflate) {


    override fun initTitleBar(): TitleBar {
        return binding.titleBar
    }

    override fun initView(binding: OrderFragmentOrderBinding) {
        LogUtils.d("OrderFragment 初始化成功")
    }

    override fun initData(binding: OrderFragmentOrderBinding) {

    }
}