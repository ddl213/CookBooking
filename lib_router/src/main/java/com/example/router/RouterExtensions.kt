package com.example.router

import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle

object RouterExtensions {
    fun Fragment.getRouteArgument(key: String, default: String = ""): String {
        return arguments?.getString(key) ?: default
    }

    fun SavedStateHandle.getRouteArgument(key: String, default: String = ""): String {
        return get<String>(key) ?: default
    }

    fun <T> Fragment.getParcelableArgument(key: String): T? {
        return arguments?.getParcelable(key)
    }
}