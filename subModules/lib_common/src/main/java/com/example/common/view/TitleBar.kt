package com.example.common.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.common.R
import com.example.common.databinding.CommonLayoutTitleBarBinding

class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var mClickCallback: (() -> Unit)? = null
    private val mBinding = CommonLayoutTitleBarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        mBinding.apply {
            tvTitle.text = typedArray.getString(R.styleable.TitleBar_title_text)
            tvTitle.textSize = typedArray.getFloat(R.styleable.TitleBar_title_text_size, 18f)

            val showRight = typedArray.hasValue(R.styleable.TitleBar_right_text)

            tvTitle.setTextColor(
                typedArray.getColor(
                    R.styleable.TitleBar_title_text_color,
                    Color.parseColor("#222222")
                )
            )
            val backResId = typedArray.getResourceId(
                R.styleable.TitleBar_left_src,
                -1
            )
            if (backResId > 0) {

                ivLeft.setImageResource(
                    backResId
                )
            }
            if (showRight){
                tvRight.text = typedArray.getString(R.styleable.TitleBar_right_text)
                tvTitle.setTextColor(
                    typedArray.getColor(
                        R.styleable.TitleBar_right_text_color,
                        Color.parseColor("#222222")
                    )
                )
                tvTitle.textSize = typedArray.getFloat(R.styleable.TitleBar_title_text_size, 18f)
            }

            typedArray.recycle()

            mBinding.ivLeft.setOnClickListener {
                mClickCallback?.invoke()
            }
        }
    }

    fun setLeftClick(callback: () -> Unit) = also {
        mClickCallback = callback
    }

    fun setLeftSrc(resId: Int) = also {
        if (resId > 0) {
            mBinding.ivLeft.setImageResource(resId)
        } else {
            mBinding.ivLeft.setImageDrawable(null)
        }
    }

    fun setTitle(content: String) = also {
        mBinding.tvTitle.text = content
    }

    fun setRightText(content: String) = also {
        mBinding.tvRight.text = content
    }

}