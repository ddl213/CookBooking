package com.example.index

import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath
import com.example.common.utils.LogUtils
import com.example.common.view.TitleBar
import com.example.index.databinding.IndexFragmentIndexBinding
import com.marky.route.annotation.Route

@Route(RoutePath.PAGE_INDEX)
class IndexFragment : BaseBindFragment<IndexFragmentIndexBinding>(IndexFragmentIndexBinding::inflate) {


    override fun initTitleBar(): TitleBar {
        return binding.titleBar
    }

    override fun initView(binding: IndexFragmentIndexBinding) {
        LogUtils.d("IndexFragment 初始化成功")
    }

    override fun initData(binding: IndexFragmentIndexBinding) {

    }
}