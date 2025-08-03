package com.example.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.common.R
import com.example.common.databinding.CommonLayoutTitleBarBinding
import com.example.common.ext.visible

class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mClickCallback: (() -> Unit)? = null
    private val mBinding = CommonLayoutTitleBarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        mBinding.apply {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
            val leftSrc = typedArray.getResourceId(R.styleable.TitleBar_left_src, -1)
            val text = typedArray.getString(R.styleable.TitleBar_title_text)
            val textColor = typedArray.getColor(R.styleable.TitleBar_title_text_color, ContextCompat.getColor(context,R.color.textTitle))
            val textSize = typedArray.getDimension(R.styleable.TitleBar_title_text_size, 18f)

            val showRight = typedArray.hasValue(R.styleable.TitleBar_right_text)

            if (leftSrc != -1) {
                ivLeft.setImageResource(leftSrc)
            }

            if (text.isNullOrEmpty().not()) {
                tvTitle.text = text
                tvTitle.setTextColor(textColor)
                tvTitle.textSize = textSize
            }

            if (showRight) {
                tvRight.text = typedArray.getString(R.styleable.TitleBar_right_text)
                tvRight.visibility = VISIBLE
                tvRight.setTextColor(textColor)
            }

            ivLeft.setOnClickListener {
                mClickCallback?.invoke()
            }

            typedArray.recycle()
        }
    }

    fun setLeftSrc(@DrawableRes resId: Int?) {
        if (resId != null) {
            mBinding.ivLeft.setImageResource(resId)
        } else {
            mBinding.ivLeft.setImageDrawable(null)
        }
    }

    fun setRightText(text: String) {
        mBinding.tvRight.text = text
        mBinding.tvRight.visible()
    }

    fun setTitle(text: String){
        mBinding.tvTitle.text = text
    }

    fun setLeftClickListener(block: () -> Unit) {
        mClickCallback = block
    }

}