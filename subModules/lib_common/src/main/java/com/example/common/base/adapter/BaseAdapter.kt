package com.example.common.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.common.listener.BaseListenerImp
import com.example.common.listener.OnItemChildClickListener
import com.example.common.listener.OnItemClickListener
import com.example.common.listener.OnItemDoubleClickListener
import com.example.common.listener.OnItemLongClickListener
import com.example.common.listener.OnItemScaleListener

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<ViewBinding>>(), BaseListenerImp {

    // Adapter的数据源列表
    protected var list: MutableList<T> = mutableListOf()

    // item的点击事件
    protected var mOnItemClickListener: OnItemClickListener? = null
    protected var mOnItemScaleListener: OnItemScaleListener? = null
    protected var mOnItemLongClickListener: OnItemLongClickListener? = null
    protected var mOnItemDoubleClickListener: OnItemDoubleClickListener? = null
    protected var mOnItemChildClickListener: OnItemChildClickListener? = null


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
    override fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    override fun setOnItemScaleListener(listener: OnItemScaleListener) {
        mOnItemScaleListener = listener
    }

    override fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mOnItemLongClickListener = listener
    }

    override fun setOnItemDoubleClickListener(listener: OnItemDoubleClickListener) {
        mOnItemDoubleClickListener = listener
    }

    override fun setOnItemChildClickListener(listener: OnItemChildClickListener) {
        mOnItemChildClickListener = listener
    }

    // 获取监听器
    protected fun getOnItemClick(): OnItemClickListener? {
        return mOnItemClickListener
    }
    protected fun getOnItemLongClick(): OnItemLongClickListener? {
        return mOnItemLongClickListener
    }
    protected fun getOnItemScale(): OnItemScaleListener? {
        return mOnItemScaleListener
    }
    protected fun getOnItemDoubleClick(): OnItemDoubleClickListener? {
        return mOnItemDoubleClickListener
    }
    protected fun getOnItemChildClick(): OnItemChildClickListener? {
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
    init {
        init?.invoke(this)
    }
}

