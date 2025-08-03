package com.example.common.base.adapter

import androidx.viewbinding.ViewBinding

class AdapterBuilder<T>{
    private var list: MutableList<T>? = null
    private var singleLayout: Class<out ViewBinding>? = null
    private val multiLayouts = mutableMapOf<Int, Class<out ViewBinding>>()
    private var viewTypeDelegate: ((position: Int, item: T) -> Int)? = null

    /**
     * 设置适配器布局。此方法支持单布局和多布局。
     * - 如果只传入一个布局，则视为单布局。
     * - 如果传入多个布局（通过 Pair），则视为多布局。
     */
    fun setLayouts(vararg layouts: Any) = also {
        if (layouts.size == 1 && layouts[0] is Class<*>) {
            singleLayout = layouts[0] as Class<out ViewBinding>
        } else {
            multiLayouts.clear()
            layouts.forEach { layout ->
                if (layout is Pair<*, *> && layout.first is Int && layout.second is Class<*>) {
                    multiLayouts[layout.first as Int] = layout.second as Class<out ViewBinding>
                }
            }
        }
    }

    /**
     * 设置获取布局类型的委托，仅在多布局模式下需要。
     */
    fun setViewTypeDelegate(delegate: (position: Int, item: T) -> Int) = also {
        viewTypeDelegate = delegate
    }

    fun setNewList(list: MutableList<T>) = also {
        this.list =  list
    }

    /**
     * 绑定数据逻辑并最终创建并返回一个完整的适配器实例。
     */
    fun bind(
        setData: (holder: BaseViewHolder<ViewBinding>, position: Int, item: T?) -> Unit
    ): BaseAdapter<T> {
        return if (multiLayouts.isNotEmpty()) {
            // 多布局模式
            if (viewTypeDelegate == null) {
                throw IllegalStateException("viewTypeDelegate must be set for multi-layout adapter.")
            }
            object : MutableAdapter<T>( viewTypeDelegate!!,list) {
                override fun setData(holder: BaseViewHolder<ViewBinding>, position: Int, item: T) {
                    setData(holder as BaseViewHolder<ViewBinding>, position, item)
                }

                override fun setLayouts(): MutableMap<Int, Class<out ViewBinding>> = multiLayouts

            }
        } else {
            // 单布局模式
            if (singleLayout == null) {
                throw IllegalStateException("Layout must be set for single-layout adapter.")
            }
            object : SingleAdapter<T, ViewBinding>(singleLayout!!,list) {
                override fun setData(holder: BaseViewHolder<ViewBinding>, position: Int, item: T) {
                    setData(holder as BaseViewHolder<ViewBinding>, position, item)
                }
            }
        }
    }
}