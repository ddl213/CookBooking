package com.android.common.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RvDecoration : RecyclerView.ItemDecoration(){
    //是否应用所有间距
    private var applyAll = false
    //间距
    private var top = 0
    private var bottom = 0
    private var left = 0
    private var right = 0
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val type = parent.layoutManager

        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }

        when (type) {
            is androidx.recyclerview.widget.GridLayoutManager -> {
                val spanCount = type.spanCount
                val column = position % spanCount
                outRect.left = column * left / spanCount
                outRect.right = right - (column + 1) * right / spanCount
                if (position < spanCount) {
                    outRect.top = top
                }
                outRect.bottom = bottom
            }

            is androidx.recyclerview.widget.LinearLayoutManager -> {
                val isVertical = type.orientation ==RecyclerView.VERTICAL

                if (isVertical){
                    if (position == 0) {
                        outRect.top = top
                    }
                    outRect.left = left
                }else{
                    if (position == 0) {
                        outRect.left = left
                    }
                    outRect.top = top
                }

                outRect.right = right
                outRect.bottom = bottom
            }
        }
    }

    fun applyAll(applyAll : Boolean) = also{
        this.applyAll = applyAll
    }

    fun setTop(top: Int) = also{
        this.top = top
    }

    fun setBottom(bottom: Int)  = also{
        this.bottom = bottom
    }

    fun setLeft(left: Int)  = also{
        this.left = left
    }

    fun setRight(right: Int)  = also{
        this.right = right
    }

    fun setVertical(vertical: Int)  = also{
        this.top = vertical
        this.bottom = vertical
    }

    fun setHorizontal(horizontal: Int)  = also{
        this.left = horizontal
        this.right = horizontal
    }
}