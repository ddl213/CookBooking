package com.example.index.fragment

import android.view.View
import androidx.viewpager2.widget.MarginPageTransformer
import com.android.common.base.BaseBindFragment
import com.android.common.base.CommonFragmentAdapter
import com.android.common.bean.TabInfo
import com.android.common.ext.attach
import com.android.common.ext.gone
import com.android.common.ext.setNavigator
import com.android.common.ext.visible
import com.android.common.utils.LogUtils
import com.campaign.common.constants.RoutePath.PAGE_INDEX
import com.campaign.common.constants.RoutePath.PAGE_INDEX_CHILD
import com.campaign.common.ext.dp
import com.example.index.bean.IndexTitleView
import com.example.index.databinding.IndexFragmentIndexBinding
import com.marky.route.annotation.Route
import com.marky.route.api.NRoute
import kotlin.math.abs

@Route(PAGE_INDEX)
class IndexFragment : BaseBindFragment<IndexFragmentIndexBinding>(IndexFragmentIndexBinding::inflate) {
    private val mTab by lazy {
        listOf(
            TabInfo(
                "推荐",
                "tuijian",
                NRoute.getFragment(PAGE_INDEX_CHILD)!!
            ),
            TabInfo(
                "好友",
                "haoyou",
                NRoute.getFragment(PAGE_INDEX_CHILD)!!
            ),
            TabInfo("热门", "hot", NRoute.getFragment(PAGE_INDEX_CHILD)!!),
            TabInfo("最新", "new", NRoute.getFragment(PAGE_INDEX_CHILD)!!)
        )
    }



    override fun initTitleBar(): View {
        return binding.vTitleBar
    }

    override fun initView(binding: IndexFragmentIndexBinding) {
        LogUtils.d("IndexFragment 初始化成功")

        binding.apply {
            indicator.setNavigator(requireContext(),mTab){tabInfo, p1 ->
                IndexTitleView(
                    context = requireContext(),
                    title = tabInfo.name,
                ).apply {
                    setOnClickListener {
                        binding.viewPager2.setCurrentItem(p1, false)
                    }
                }
            }

            viewPager2.adapter = CommonFragmentAdapter(mTab,this@IndexFragment)
            viewPager2.attach(indicator)
            viewPager2.setPageTransformer(MarginPageTransformer(8.dp().toInt()))


        }

    }

    override fun initData(binding: IndexFragmentIndexBinding) {

    }

    override fun initListener(binding: IndexFragmentIndexBinding) {
        super.initListener(binding)
        binding.apply {
            //设置折叠监听
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                val totalScrollRange = appBarLayout.totalScrollRange
                val alpha = abs(verticalOffset / totalScrollRange.toFloat())

                ivSearchPin.alpha = alpha
                if (alpha > 0.5){
                    ivSearchPin.visible()
                }else if (alpha < 0.2){
                    ivSearchPin.gone()
                }
            }
        }
    }
}