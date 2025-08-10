package com.android.common.base.adapter

import androidx.viewbinding.ViewBinding
import com.android.common.utils.LogUtils

class AdapterBuilder<T> {
    @PublishedApi
    internal var list: MutableList<T>? = null
    @PublishedApi
    internal var singleLayout: Class<out ViewBinding>? = null
    @PublishedApi
    internal val multiLayouts = mutableMapOf<Int, Class<out ViewBinding>>()
    @PublishedApi
    internal var viewTypeDelegate: ((position: Int, item: T) -> Int)? = null

    // 设置单布局
    fun setLayout(layout: Class<out ViewBinding>) = apply {
        singleLayout = layout
        multiLayouts.clear()
    }

    // 设置多布局
    fun setLayout(viewType: Int, layout: Class<out ViewBinding>) = apply {
        multiLayouts[viewType] = layout
        singleLayout = null
    }

    // 设置获取布局类型的委托（多布局必需）
    fun setViewTypeDelegate(delegate: (position: Int, item: T) -> Int) = apply {
        viewTypeDelegate = delegate
    }

    fun setList(list: MutableList<T>) = apply {
        this.list = list
    }

    // 类型安全的单布局绑定
    inline fun <reified VB : ViewBinding> bind(
        crossinline setData: (holder: BaseViewHolder<VB>, position: Int, item: T) -> Unit
    ): BaseAdapter<T> {
        if (singleLayout == null) {
            throw IllegalStateException("Single layout must be set using setLayout()")
        }
        return object : SingleAdapter<T, ViewBinding>(singleLayout!!, list) {
            override fun setData(holder: BaseViewHolder<ViewBinding>, position: Int, item: T) {
                val typedHolder = holder as BaseViewHolder<VB>
                setData(typedHolder, position, item)
            }
        }
    }

    // 类型安全的多布局绑定（Flow式类型自动转换）
    inline fun bindMulti(
        crossinline setData : (holder: BaseViewHolder<ViewBinding>, position: Int, item: T) -> Unit
    ): BaseAdapter<T> {
        if (multiLayouts.isEmpty()) {
            throw IllegalStateException("Multi-layouts must be added using addLayout()")
        }
        if (viewTypeDelegate == null) {
            throw IllegalStateException("viewTypeDelegate must be set for multi-layout adapter.")
        }

        return object : MutableAdapter<T>(viewTypeDelegate!!, list) {
            override fun setData(holder: BaseViewHolder<ViewBinding>, position: Int, item: T) {
                LogUtils.d("setData: $position")
                setData(holder, position, item)
            }

            override fun setLayouts(): MutableMap<Int, Class<out ViewBinding>> = multiLayouts
        }
    }
}