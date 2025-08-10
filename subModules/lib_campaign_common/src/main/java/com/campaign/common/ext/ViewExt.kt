package com.campaign.common.ext

import android.net.Uri
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide











/**
 * --------------------------------- RecyclerView ---------------------------------
 */

fun RecyclerView.liner(orientation : Int = RecyclerView.VERTICAL,reverse : Boolean = false) = also{
    layoutManager = LinearLayoutManager(context, orientation, reverse)
}

fun RecyclerView.grid(spanCount : Int = 2,orientation : Int = RecyclerView.VERTICAL,reverse : Boolean = false) = also{
    layoutManager = GridLayoutManager(context, spanCount, orientation, reverse)
}



/**
 * --------------------------------- ImageView ---------------------------------
 */
fun ImageView.load(url : String){
    Glide.with(this)
        .load( url)
        .into(this)
}

fun ImageView.load(url : Uri){
    Glide.with(this)
        .load( url)
        .into(this)
}

fun ImageView.load(url : Int){
    Glide.with(this)
        .load( url)
        .into(this)
}