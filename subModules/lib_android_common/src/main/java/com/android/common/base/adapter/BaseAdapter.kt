package com.android.common.base.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<ViewBinding>>(),
    com.android.common.listener.BaseListenerImp {

    // Adapter的数据源列表
    protected var list: MutableList<T> = mutableListOf()

    // item的点击事件
    private var mOnItemClickListener: com.android.common.listener.OnItemClickListener? = null
    private var mOnItemScaleListener: com.android.common.listener.OnItemScaleListener? = null
    private var mOnItemLongClickListener: com.android.common.listener.OnItemLongClickListener? = null
    private var mOnItemDoubleClickListener: com.android.common.listener.OnItemDoubleClickListener? = null
    private var mOnItemChildClickListener: com.android.common.listener.OnItemChildClickListener? = null


    // 设置ViewHolder初始化的回调
    protected var initViewHolder: ((BaseViewHolder<ViewBinding>) -> Unit)? = null

    fun initViewHolder(block: (BaseViewHolder<ViewBinding>) -> Unit) {
        initViewHolder = block
    }

    override fun getItemCount(): Int = list.size

    // 绑定控件点击事件
    protected fun bindViewClickListener(holder: BaseViewHolder<ViewBinding>) {
        // item点击事件
        mOnItemClickListener?.let {
            holder.itemView.setOnClickListener { v ->
                val position = holder.absoluteAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                setOnItemClick(v, position)
            }
        }
        // item的长按事件
        mOnItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener { v ->
                val position = holder.absoluteAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnLongClickListener false
                setOnItemLongClick(v, position)
            }
        }
        // item子控件的点击事件
        mOnItemChildClickListener?.let {
            for (id in getChildClickViewIds()) {
                holder.itemView.findViewById<View>(id)?.let {
                    if (!it.isClickable) {
                        it.isClickable = true
                    }
                    it.setOnClickListener { v ->
                        val position = holder.absoluteAdapterPosition
                        if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                        setOnItemChildClick(v, position)
                    }
                }
            }
        }
    }

    // 调用itemView点击事件
    private fun setOnItemClick(v: View, position: Int) {
        mOnItemClickListener?.onItemClick(this, v, position)
    }

    private fun setOnItemLongClick(v: View, position: Int): Boolean {
        return mOnItemLongClickListener?.onItemLongClick(this, v, position) ?: false
    }

    private fun setOnItemScale(v: View, position: Int, scaleFactor: Float) {
        mOnItemScaleListener?.onItemScale(this, v, position, scaleFactor)
    }

    private fun setOnItemDoubleClick(v: View, position: Int) {
        mOnItemDoubleClickListener?.onItemDoubleClick(this, v, position)
    }

    private fun setOnItemChildClick(v: View, position: Int) {
        mOnItemChildClickListener?.onItemChildClick(this, v, position)
    }

    // 外界设置点击监听事件
    override fun setOnItemClickListener(listener: com.android.common.listener.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    override fun setOnItemScaleListener(listener: com.android.common.listener.OnItemScaleListener) {
        mOnItemScaleListener = listener
    }

    override fun setOnItemLongClickListener(listener: com.android.common.listener.OnItemLongClickListener) {
        mOnItemLongClickListener = listener
    }

    override fun setOnItemDoubleClickListener(listener: com.android.common.listener.OnItemDoubleClickListener) {
        mOnItemDoubleClickListener = listener
    }

    override fun setOnItemChildClickListener(listener: com.android.common.listener.OnItemChildClickListener) {
        mOnItemChildClickListener = listener
    }

    // 获取监听器
    protected fun getOnItemClick(): com.android.common.listener.OnItemClickListener? {
        return mOnItemClickListener
    }
    protected fun getOnItemLongClick(): com.android.common.listener.OnItemLongClickListener? {
        return mOnItemLongClickListener
    }
    protected fun getOnItemScale(): com.android.common.listener.OnItemScaleListener? {
        return mOnItemScaleListener
    }
    protected fun getOnItemDoubleClick(): com.android.common.listener.OnItemDoubleClickListener? {
        return mOnItemDoubleClickListener
    }
    protected fun getOnItemChildClick(): com.android.common.listener.OnItemChildClickListener? {
        return mOnItemChildClickListener
    }

    // 需要点击事件的View
    private val childClickViewIds = LinkedHashSet<Int>()

    private fun getChildClickViewIds(): LinkedHashSet<Int> {
        return childClickViewIds
    }

    fun addChildClickViewIds(@IdRes vararg ids: Int) {
        childClickViewIds.addAll(ids.toList())
    }

    // 设置adapter数据源
    @SuppressLint("NotifyDataSetChanged")
    open fun setNewInstance(list: MutableList<T>?) {
        if (list === this.list) {
            return
        }
        this.list = list ?: arrayListOf()
        notifyDataSetChanged()
    }

    open fun submits(list: List<T>) {
        if (list !== this.list) {
            this.list.clear()
            if (list.isNotEmpty()) {
                this.list.addAll(list)
            }
        } else {
            if (list.isNotEmpty()) {
                val newInstance = ArrayList(list)
                this.list.clear()
                this.list.addAll(newInstance)
            } else {
                this.list.clear()
            }
        }
    }

    fun removeItemByPos(position: Int) {
        list.removeAt(position)
    }

    fun removeItemByValue(item: T?) {
        list.remove(item)
    }

    fun clear() {
        list.clear()
    }
}

class BaseViewHolder<VB : ViewBinding>(
    val binding: VB,
    init: ((BaseViewHolder<VB>) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {
    //为了多布局能够更方便的获取到binding
    //一个布局会生成一个holder，所以创建一个接口，将holder的binding进行强转
    private val handler : MultiLayoutHandler
    init {
        init?.invoke(this)
        handler = object : MultiLayoutHandler {
            override fun <VB : ViewBinding> withBinding(
                block: VB.() -> Unit
            ) {
                block(binding as VB)
            }
        }
    }

    fun <VB : ViewBinding> withBinding(
        block: VB.() -> Unit
    ){
        handler.withBinding(block)
    }

    // 多布局处理接口
    interface MultiLayoutHandler {
        fun <VB : ViewBinding> withBinding(
            block: VB.() -> Unit
        )
    }
}

