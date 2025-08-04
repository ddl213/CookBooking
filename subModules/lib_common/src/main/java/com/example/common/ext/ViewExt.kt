package com.example.common.ext

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.example.common.base.adapter.AdapterBuilder
import com.example.common.base.adapter.BaseAdapter
import com.example.common.base.adapter.BaseViewHolder
import com.example.common.base.adapter.SingleAdapter
import com.example.common.bean.TabInfo
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView


/**
 * --------------------------------- View ---------------------------------
 */
fun View.visible(){
    if (visibility == View.VISIBLE) return
    visibility = View.VISIBLE
}

fun View.gone(){
    if (visibility == View.GONE) return
    visibility = View.GONE
}


/**
 * --------------------------------- ViewPager2 ---------------------------------
 */
fun ViewPager2.attach(indicator: MagicIndicator) {
    this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int, positionOffset: Float, positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            indicator.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            indicator.onPageScrollStateChanged(state)
        }
    })
}







/**
 * --------------------------------- RecyclerView ---------------------------------
 */

fun RecyclerView.liner(orientation : Int = RecyclerView.VERTICAL,reverse : Boolean = false) = also{
    layoutManager = LinearLayoutManager(context, orientation, reverse)
}

fun RecyclerView.grid(spanCount : Int = 2,orientation : Int = RecyclerView.VERTICAL,reverse : Boolean = false) = also{
    layoutManager = GridLayoutManager(context, spanCount, orientation, reverse)
}

fun <T> RecyclerView.buildAdapter(block: AdapterBuilder<T>.() -> BaseAdapter<T>): BaseAdapter<T> {
    val builder = AdapterBuilder<T>()
    val adapter = builder.block()
    this.adapter = adapter
    return adapter
}

inline fun <T, reified VB : ViewBinding> adapterOf(
    clazz: Class<VB> = VB::class.java,
    list: MutableList<T>? = null,
    crossinline setData: BaseAdapter<T>.(holder: BaseViewHolder<VB>, position: Int, item: T) -> Unit,
): SingleAdapter<T, VB> =

    object : SingleAdapter<T, VB>(clazz, list ?: mutableListOf<T>()) {
        @Suppress("UNCHECKED_CAST")
        override fun setData(holder: BaseViewHolder<ViewBinding>, position: Int, item: T) {
            val typedHolder = holder as BaseViewHolder<VB>
            setData(this, typedHolder, position, item)
        }
    }


/**
 * --------------------------------- MagicIndicator ---------------------------------
 */

fun MagicIndicator.setNavigator(context : Context, tabList: List<TabInfo>,isAdjustMode : Boolean = true,block : (TabInfo,Int) -> IPagerTitleView){

    navigator = CommonNavigator(context).apply {
            adapter = object : CommonNavigatorAdapter() {
                override fun getCount(): Int {
                    return tabList.size
                }

                override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                    val item = tabList[ index]
                    return block.invoke( item,index)
                }

                override fun getIndicator(context: Context?): IPagerIndicator? {
                    return null
                }
            }
        this.isAdjustMode = isAdjustMode
    }

}