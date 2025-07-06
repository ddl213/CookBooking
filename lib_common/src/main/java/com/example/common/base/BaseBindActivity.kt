package com.example.common.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseBindActivity<V : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> V) :
    AppCompatActivity() {

    private var _binding: V? = null//私有的binding用于获取传进来的binding
    //只读的binding，用于暴露出去
    protected val binding get() = _binding ?: throw IllegalStateException("Binding is null. Is the dialog shown or already destroyed?")

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        _binding = inflate.invoke(layoutInflater, null, false)
        setContentView(binding.root)
        initView(binding)
        initListener(binding)
        initData(binding)
    }


    /**
     * 初始化view
     */
    abstract fun initView(binding : V)

    /**
     * 初始化数据
     */
    abstract fun initData(binding : V)

    /**
     * 初始化监听器
     */
    abstract fun initListener(binding : V)

    //将binding置为空,防止内存消耗
    override fun onDestroy() {
        super.onDestroy()
        //置空
        _binding = null
    }
}