package com.rhys.cookbook

import android.view.View
import com.android.common.base.BaseBindFragment
import com.rhys.cookbook.databinding.CookbookFragmentCookBookChildBinding

/**
 * 展示我的菜谱数据
 * 已发布的菜谱：点击会跳转到菜谱详情页面
 * 草稿状态：点击会携带当前信息，跳转到编辑页面
 *
 */
class CookBookChildFragment : BaseBindFragment<CookbookFragmentCookBookChildBinding>(CookbookFragmentCookBookChildBinding::inflate) {

    override fun initTitleBar(): View? = null
    override fun initView(binding: CookbookFragmentCookBookChildBinding) {

    }

    override fun initData(binding: CookbookFragmentCookBookChildBinding) {

    }

}