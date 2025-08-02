package com.rhys.cookbook

import com.example.common.base.BaseBindFragment
import com.example.common.constants.RoutePath
import com.example.common.utils.LogUtils
import com.example.common.view.TitleBar
import com.marky.route.annotation.Route
import com.rhys.cookbook.databinding.CookbookFragmentCookBookBinding

@Route(RoutePath.PAGE_COOK_BOOK)
class CookBookFragment : BaseBindFragment<CookbookFragmentCookBookBinding>(CookbookFragmentCookBookBinding::inflate) {


    override fun initTitleBar(): TitleBar {
        return binding.titleBar
    }

    override fun initView(binding: CookbookFragmentCookBookBinding) {
        LogUtils.d("CookBookFragment 初始化成功")
    }

    override fun initData(binding: CookbookFragmentCookBookBinding) {

    }
}