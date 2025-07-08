package com.example.annotations

/**
 * 标记可路由的目标类
 * @param path 路由路径，必须唯一
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class Route(val path: String)