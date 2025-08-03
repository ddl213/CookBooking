package com.example.common.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

abstract class SingleAdapter<T, VB : ViewBinding>(
    private val viewBindingClass: Class<out VB>,
    list: MutableList<T>? = null
) : BaseAdapter<T>() {

    init {
        super.setNewInstance(list)
    }
    // 通过反射获取inflate方法，并缓存以提高性能
    private val inflateMethod by lazy {
        viewBindingClass.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {
        val binding = inflateMethod.invoke(
            null,
            LayoutInflater.from(parent.context),
            parent,
            false
        ) as VB
        val holder = BaseViewHolder(binding, initViewHolder)
        bindViewClickListener(holder)
        return holder as BaseViewHolder<ViewBinding>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {

        setData(holder, position, list[position])
    }

    // 绑定数据的方法，由子类实现
    abstract fun setData(holder: BaseViewHolder<ViewBinding>, position: Int, item: T)

    // 这里的setData方法是用于子类实现的
//    abstract fun setData(holder: BaseViewHolder<VB>, position: Int, item: T?)
//
//    // 重写父类的setData方法，强制转换
//    override fun setData(holder: BaseViewHolder<ViewBinding>, position: Int, item: T?) {
//        @Suppress("UNCHECKED_CAST")
//        setData(holder as BaseViewHolder<VB>, position, item)
//    }
}

