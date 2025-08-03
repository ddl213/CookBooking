package com.example.index.bean

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.common.ext.gone
import com.example.common.ext.visible
import com.example.common.utils.LogUtils
import com.example.index.databinding.IndexLayoutTitleViewBinding
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView

class IndexTitleView@JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    def: Int = 0,
    private val title: String = ""
) : FrameLayout(context, attributeSet, def), IPagerTitleView {
    private val mBinding =
        IndexLayoutTitleViewBinding.inflate(LayoutInflater.from(context), this, true)


    init {
        mBinding.tvTitle.text = title
    }

    override fun onSelected(p0: Int, p1: Int) {
        mBinding.tvTitle.isSelected = true
        mBinding.tvTitle.typeface = Typeface.DEFAULT_BOLD
        mBinding.vBottomLine.visible()

        LogUtils.d("onSelected $p0 :: $p1")
    }

    override fun onDeselected(p0: Int, p1: Int) {
        mBinding.tvTitle.isSelected = false
        mBinding.tvTitle.typeface = Typeface.DEFAULT
        mBinding.vBottomLine.gone()
        LogUtils.d("onDeselected $p0 :: $p1")
    }

    override fun onLeave(p0: Int, p1: Int, p2: Float, p3: Boolean) {

    }

    override fun onEnter(p0: Int, p1: Int, p2: Float, p3: Boolean) {

    }
}