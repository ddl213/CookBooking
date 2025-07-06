package com.example.router

import android.app.Activity
import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 路由参数委托类
 */
class RouterArgument<T>(
    private val key: String,
    private val defaultValue: T? = null
) : ReadOnlyProperty<Any, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return when (thisRef) {
            is Fragment -> {
                thisRef.arguments?.get(key) as? T
                    ?: defaultValue
                    ?: throw IllegalArgumentException("Missing required argument: $key")
            }
            is Activity -> {
                thisRef.intent?.extras?.get(key) as? T
                    ?: defaultValue
                    ?: throw IllegalArgumentException("Missing required argument: $key")
            }
            else -> throw IllegalStateException("RouterArgument can only be used in Activity or Fragment")
        }
    }
}

/**
 * 可空路由参数委托
 */
class NullableRouterArgument<T>(
    private val key: String
) : ReadOnlyProperty<Any, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return when (thisRef) {
            is Fragment -> thisRef.arguments?.get(key) as? T
            is Activity -> thisRef.intent?.extras?.get(key) as? T
            else -> null
        }
    }
}

/**
 * 安全路由参数委托（带默认值）
 */
class SafeRouterArgument<T>(
    private val key: String,
    private val defaultValue: T
) : ReadOnlyProperty<Any, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return when (thisRef) {
            is Fragment -> thisRef.arguments?.get(key) as? T ?: defaultValue
            is Activity -> thisRef.intent?.extras?.get(key) as? T ?: defaultValue
            else -> defaultValue
        }
    }
}