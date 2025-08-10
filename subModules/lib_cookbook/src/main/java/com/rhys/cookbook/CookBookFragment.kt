package com.rhys.cookbook

import com.android.common.base.BaseBindFragment
import com.android.common.utils.LogUtils
import com.android.common.view.TitleBar
import com.campaign.common.constants.RoutePath.PAGE_COOK_BOOK
import com.marky.route.annotation.Route
import com.rhys.cookbook.databinding.CookbookFragmentCookBookBinding

@Route(PAGE_COOK_BOOK)
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