package com.campaign.common.ext

import android.content.res.Resources
import android.util.TypedValue


fun Int.dp(): Float {
    // 获取当前手机的像素密度（1个dp对应几个px）
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)
}


fun Int.sp(): Float {
    // 获取当前手机的像素密度（1个sp对应几个px）
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this.toFloat(), Resources.getSystem().displayMetrics)
}