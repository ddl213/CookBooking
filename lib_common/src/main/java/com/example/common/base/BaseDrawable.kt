package com.example.common.base


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.example.common.R
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * `BaseDrawable` 是一个自定义的 `Drawable` 类，旨在为 Android `View` 提供
 * 灵活的背景和边框绘制功能，支持纯色、渐变色以及圆角。
 *
 * 它通过解析 XML 属性来配置其外观，并能动态更新这些属性。
 */
class BaseDrawable : Drawable() {
    // 用于绘制填充区域（如背景）的画笔
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL // 设置画笔填充模式
    }

    // 用于绘制边框的画笔
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE // 设置画笔描边模式
    }

    // 用于绘制圆角路径的路径对象
    private val path = Path()

    // 用于表示 `Drawable` 边界的矩形区域
    private val rectF = RectF()

    // 用于表示边框绘制区域的矩形（比 `rectF` 稍微小一些，以居中绘制边框）
    private val borderRectF = RectF()

    // --- 背景属性 ---
    @ColorInt
    private var backgroundColor: Int = Color.TRANSPARENT // 纯色背景颜色，默认为透明

    @ColorInt
    private var backgroundStartColor: Int = Color.TRANSPARENT // 背景渐变的起始颜色

    @ColorInt
    private var backgroundCenterColor: Int = Color.TRANSPARENT // 背景渐变的中心颜色（用于三色渐变）

    @ColorInt
    private var backgroundEndColor: Int = Color.TRANSPARENT // 背景渐变的结束颜色

    private var backgroundGradientAngle: Float = 0f // 背景渐变的角度（0-360度）

    private var backgroundDrawable: Drawable? = null // 可选的背景 Drawable，如果设置，则覆盖所有颜色和边框样式

    // --- 边框属性 ---
    private var borderWidth: Float = 0f // 边框宽度，默认为0（无边框）

    @ColorInt
    private var borderColor: Int = Color.TRANSPARENT // 纯色边框颜色，默认为透明

    @ColorInt
    private var borderStartColor: Int = Color.TRANSPARENT // 边框渐变的起始颜色

    @ColorInt
    private var borderCenterColor: Int = Color.TRANSPARENT // 边框渐变的中心颜色（用于三色渐变）

    @ColorInt
    private var borderEndColor: Int = Color.TRANSPARENT // 边框渐变的结束颜色

    private var borderGradientAngle: Float = 0f // 边框渐变的角度（0-360度）

    // --- 圆角属性 ---
    private var cornerRadius: Float = 0f // 统一圆角半径，默认为0（无圆角）

    // 各个角落的圆角半径数组，按 (topLeft, topLeft, topRight, topRight, ...) 顺序
    private var cornerRadii: FloatArray? = null

    /**
     * 从 XML 属性中解析并应用样式。
     * 这个方法通常在宿主 `View` 的构造函数中被调用。
     *
     * @param context `Context` 实例，用于获取资源。
     * @param attrs `AttributeSet` 实例，包含从 XML 读取的属性。
     */
    fun applyAttributes(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return // 如果没有属性，则直接返回

        // 获取自定义样式属性的 TypedArray
        val style = context.obtainStyledAttributes(attrs, R.styleable.CommonGradientAttrs)
        try {
            // --- 背景属性处理 ---
            // 尝试获取 `cg_backgroundDrawable` 属性，如果设置了，则优先使用 Drawable 作为背景
            val bgDrawableRef = style.getResourceId(R.styleable.CommonGradientAttrs_background_drawable, 0)
            if (bgDrawableRef != 0) {
                backgroundDrawable = ResourcesCompat.getDrawable(context.resources,bgDrawableRef, context.theme)
                // 如果设置了 Drawable 背景，则清除所有其他颜色、边框和圆角属性，避免冲突
                backgroundColor = Color.TRANSPARENT
                backgroundStartColor = Color.TRANSPARENT
                backgroundCenterColor = Color.TRANSPARENT
                backgroundEndColor = Color.TRANSPARENT
                backgroundGradientAngle = 0f
                borderWidth = 0f
                cornerRadius = 0f
                cornerRadii = null
            } else {
                // 如果没有设置 Drawable 背景，则处理颜色和渐变属性
                backgroundDrawable = null // 确保 Drawable 背景为 null

                // 检查是否存在纯色背景或渐变背景的属性
                val hasBgColor = style.hasValue(R.styleable.CommonGradientAttrs_background)
                val hasBgStartColor = style.hasValue(R.styleable.CommonGradientAttrs_start_color)
                val hasBgCenterColor = style.hasValue(R.styleable.CommonGradientAttrs_center_color)
                val hasBgEndColor = style.hasValue(R.styleable.CommonGradientAttrs_end_color)

                if (hasBgColor) {
                    // 如果设置了纯色背景，则将所有渐变颜色也设置为该纯色，以便统一绘制逻辑
                    backgroundColor = style.getColor(R.styleable.CommonGradientAttrs_background, Color.TRANSPARENT)
                    backgroundStartColor = backgroundColor
                    backgroundCenterColor = backgroundColor
                    backgroundEndColor = backgroundColor
                    backgroundGradientAngle = 0f // 纯色时角度不重要
                } else if (hasBgStartColor || hasBgCenterColor || hasBgEndColor) {
                    // 如果存在任何渐变颜色属性，则按渐变模式处理
                    backgroundStartColor = style.getColor(R.styleable.CommonGradientAttrs_start_color, Color.TRANSPARENT)
                    backgroundCenterColor = style.getColor(R.styleable.CommonGradientAttrs_center_color, Color.TRANSPARENT)
                    backgroundEndColor = style.getColor(R.styleable.CommonGradientAttrs_end_color, Color.TRANSPARENT)
                    backgroundGradientAngle = style.getFloat(R.styleable.CommonGradientAttrs_angle, 0f)
                    backgroundColor = Color.TRANSPARENT // 渐变时，纯色背景颜色设为透明
                } else {
                    // 如果没有任何背景相关属性，则所有背景颜色默认为透明
                    backgroundColor = Color.TRANSPARENT
                    backgroundStartColor = Color.TRANSPARENT
                    backgroundCenterColor = Color.TRANSPARENT
                    backgroundEndColor = Color.TRANSPARENT
                    backgroundGradientAngle = 0f
                }

                // --- 边框属性处理 (仅当没有设置 backgroundDrawable 时才处理) ---
                borderWidth = style.getDimension(R.styleable.CommonGradientAttrs_border, 0f)

                // 检查是否存在纯色边框或渐变边框的属性
                val hasBorderColor = style.hasValue(R.styleable.CommonGradientAttrs_border_color)
                val hasBorderStartColor = style.hasValue(R.styleable.CommonGradientAttrs_border_start_color)
                val hasBorderCenterColor = style.hasValue(R.styleable.CommonGradientAttrs_border_center_color)
                val hasBorderEndColor = style.hasValue(R.styleable.CommonGradientAttrs_border_end_color)

                if (hasBorderColor) {
                    // 如果设置了纯色边框，则将所有渐变颜色也设置为该纯色
                    borderColor = style.getColor(R.styleable.CommonGradientAttrs_border_color, Color.TRANSPARENT)
                    borderStartColor = borderColor
                    borderCenterColor = borderColor
                    borderEndColor = borderColor
                    borderGradientAngle = 0f
                } else if (hasBorderStartColor || hasBorderCenterColor || hasBorderEndColor) {
                    // 如果存在任何渐变边框颜色属性，则按渐变模式处理
                    borderStartColor = style.getColor(R.styleable.CommonGradientAttrs_border_start_color, Color.TRANSPARENT)
                    borderCenterColor = style.getColor(R.styleable.CommonGradientAttrs_border_center_color, Color.TRANSPARENT)
                    borderEndColor = style.getColor(R.styleable.CommonGradientAttrs_border_end_color, Color.TRANSPARENT)
                    borderGradientAngle = style.getFloat(R.styleable.CommonGradientAttrs_border_color_angle, 0f)
                    borderColor = Color.TRANSPARENT // 渐变时，纯色边框颜色设为透明
                } else {
                    // 如果没有任何边框相关属性，则所有边框颜色默认为透明
                    borderColor = Color.TRANSPARENT
                    borderStartColor = Color.TRANSPARENT
                    borderCenterColor = Color.TRANSPARENT
                    borderEndColor = Color.TRANSPARENT
                    borderGradientAngle = 0f
                }

                // --- 圆角属性处理 (仅当没有设置 backgroundDrawable 时才处理) ---
                cornerRadius = style.getDimension(R.styleable.CommonGradientAttrs_radius, 0f)
                val topLeftRadius = style.getDimension(R.styleable.CommonGradientAttrs_radius_top_left, cornerRadius)
                val topRightRadius = style.getDimension(R.styleable.CommonGradientAttrs_radius_top_right, cornerRadius)
                val bottomRightRadius = style.getDimension(R.styleable.CommonGradientAttrs_radius_bottom_right, cornerRadius)
                val bottomLeftRadius = style.getDimension(R.styleable.CommonGradientAttrs_radius_bottom_left, cornerRadius)

                // 根据读取到的圆角值设置 `cornerRadii` 数组
                cornerRadii = if (cornerRadius != 0f || topLeftRadius != 0f || topRightRadius != 0f || bottomRightRadius != 0f || bottomLeftRadius != 0f) {
                    if (cornerRadius != 0f && (topLeftRadius == cornerRadius && topRightRadius == cornerRadius && bottomRightRadius == cornerRadius && bottomLeftRadius == cornerRadius)) {
                        // 如果设置了统一圆角且四个角相等，则直接使用统一圆角
                        floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius, cornerRadius, cornerRadius)
                    } else {
                        // 否则使用单独设置的每个角圆角
                        floatArrayOf(topLeftRadius, topLeftRadius,
                            topRightRadius, topRightRadius,
                            bottomRightRadius, bottomRightRadius,
                            bottomLeftRadius, bottomLeftRadius)
                    }
                } else {
                    null // 没有圆角
                }
            }
        } finally {
            // 确保 TypedArray 被回收，避免内存泄露
            style.recycle()
        }
        invalidateSelf() // 属性更新后，请求 Drawable 重绘
    }

    /**
     * `Drawable` 的核心绘制方法。当 `View` 需要重绘时，会调用此方法。
     *
     * @param canvas 绘制的目标画布。
     */
    @Deprecated("Deprecated in Java") // 覆盖了 Java 中已弃用的成员
    override fun draw(canvas: Canvas) {
        rectF.set(bounds) // 将 `Drawable` 的边界设置到 `rectF`

        // 优先绘制外部设置的 Drawable 背景
        if (backgroundDrawable != null) {
            backgroundDrawable!!.bounds = bounds // 设置 Drawable 的边界
            backgroundDrawable!!.draw(canvas) // 绘制 Drawable
        } else {
            // --- 绘制背景 (纯色或渐变) ---
            val bgColors = if (backgroundStartColor != Color.TRANSPARENT || backgroundCenterColor != Color.TRANSPARENT || backgroundEndColor != Color.TRANSPARENT) {
                // 如果设置了渐变颜色，判断是两色渐变还是三色渐变
                if (backgroundCenterColor != Color.TRANSPARENT && backgroundStartColor != backgroundCenterColor && backgroundCenterColor != backgroundEndColor) {
                    intArrayOf(backgroundStartColor, backgroundCenterColor, backgroundEndColor) // 三色渐变
                } else {
                    intArrayOf(backgroundStartColor, backgroundEndColor) // 默认两色渐变
                }
            } else if (backgroundColor != Color.TRANSPARENT) {
                intArrayOf(backgroundColor, backgroundColor) // 纯色背景（用两个相同颜色模拟渐变，方便统一处理）
            } else {
                null // 透明背景
            }

            if (bgColors != null) {
                // 如果有背景颜色，则创建线性渐变并设置给画笔的 Shader
                paint.shader = createLinearGradient(bgColors, backgroundGradientAngle, rectF)
            } else {
                paint.shader = null // 清除 Shader
                paint.color = Color.TRANSPARENT // 纯色背景设为透明
            }

            // 根据是否设置了圆角来绘制背景
            if (cornerRadii != null) {
                path.reset() // 重置路径
                path.addRoundRect(rectF, cornerRadii!!, Path.Direction.CW) // 添加圆角矩形路径
                canvas.drawPath(path, paint) // 绘制路径
            } else {
                canvas.drawRect(rectF, paint) // 绘制普通矩形
            }

            // --- 绘制边框 (纯色或渐变) ---
            if (borderWidth > 0) {
                borderPaint.strokeWidth = borderWidth // 设置边框画笔的宽度

                val borderColors = if (borderStartColor != Color.TRANSPARENT || borderCenterColor != Color.TRANSPARENT || borderEndColor != Color.TRANSPARENT) {
                    // 如果设置了渐变颜色，判断是两色渐变还是三色渐变
                    if (borderCenterColor != Color.TRANSPARENT && borderStartColor != borderCenterColor && borderCenterColor != borderEndColor) {
                        intArrayOf(borderStartColor, borderCenterColor, borderEndColor) // 三色渐变
                    } else {
                        intArrayOf(borderStartColor, borderEndColor) // 默认两色渐变
                    }
                } else if (borderColor != Color.TRANSPARENT) {
                    intArrayOf(borderColor, borderColor) // 纯色边框
                } else {
                    null // 透明边框
                }

                if (borderColors != null) {
                    // 如果有边框颜色，则创建线性渐变并设置给边框画笔的 Shader
                    borderPaint.shader = createLinearGradient(borderColors, borderGradientAngle, rectF)
                } else {
                    borderPaint.shader = null // 清除 Shader
                    borderPaint.color = Color.TRANSPARENT // 纯色边框设为透明
                }

                // 计算边框的绘制区域，向内缩进一半的边框宽度以使边框居中
                borderRectF.set(rectF)
                val halfBorder = borderWidth / 2f
                borderRectF.inset(halfBorder, halfBorder)

                // 根据是否设置了圆角来绘制边框
                if (cornerRadii != null) {
                    val borderCornerRadii = FloatArray(8)
                    // 计算边框的圆角半径（比背景的圆角半径小一半边框宽度）
                    for (i in cornerRadii!!.indices) {
                        borderCornerRadii[i] = 0f.coerceAtLeast(cornerRadii!![i] - halfBorder)
                    }
                    path.reset() // 重置路径
                    path.addRoundRect(borderRectF, borderCornerRadii, Path.Direction.CW) // 添加圆角矩形路径
                    canvas.drawPath(path, borderPaint) // 绘制路径
                } else {
                    canvas.drawRect(borderRectF, borderPaint) // 绘制普通矩形边框
                }
            }
        }
    }

    /**
     * 根据颜色数组、角度和边界创建 `LinearGradient`。
     * 支持两色或三色渐变。
     *
     * @param colors 渐变颜色数组。可以是2个颜色（起点和终点）或3个颜色（起点、中心点、终点）。
     * @param angle 渐变角度（0-360度）。0度为从左到右，90度为从上到下。
     * @param bounds 渐变应用的矩形区域。
     * @return 配置好的 `LinearGradient` 对象。
     */
    private fun createLinearGradient(@ColorInt colors: IntArray, angle: Float, bounds: RectF): LinearGradient {
        val x0: Float
        val y0: Float
        val x1: Float
        val y1: Float
        val width = bounds.width()
        val height = bounds.height()

        // 将角度转换为弧度
        val radians = Math.toRadians(angle.toDouble()).toFloat()
        // 获取渐变区域的中心点
        val centerX = bounds.centerX()
        val centerY = bounds.centerY()
        // 计算从中心到矩形对角线最远点的距离，作为渐变线的最大长度
        val maxLen = hypot((width / 2f).toDouble(), (height / 2f).toDouble()).toFloat()

        // 根据角度和最大长度计算渐变线的起始点 (x0, y0) 和结束点 (x1, y1)
        // 这样可以确保渐变覆盖整个矩形区域，无论角度如何
        x0 = centerX - cos(radians.toDouble()).toFloat() * maxLen
        y0 = centerY - sin(radians.toDouble()).toFloat() * maxLen
        x1 = centerX + cos(radians.toDouble()).toFloat() * maxLen
        y1 = centerY + sin(radians.toDouble()).toFloat() * maxLen

        // 如果是三色渐变，需要指定颜色在渐变线上的位置
        // 0f 表示起始位置，0.5f 表示中间位置，1f 表示结束位置
        val positions: FloatArray? = if (colors.size == 3) floatArrayOf(0f, 0.5f, 1f) else null

        // 创建并返回线性渐变对象
        return LinearGradient(x0, y0, x1, y1, colors, positions, Shader.TileMode.CLAMP)
    }

    /**
     * 设置 Drawable 的 alpha (透明度)。
     *
     * @param alpha 透明度值 (0-255)。
     */
    @Deprecated("Deprecated in Java") // 覆盖了 Java 中已弃用的成员
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha // 设置填充画笔的透明度
        borderPaint.alpha = alpha // 设置边框画笔的透明度
        invalidateSelf() // 请求 Drawable 重绘
    }

    /**
     * 获取 Drawable 的不透明度。
     *
     * @return 不透明度常量，如 `PixelFormat.OPAQUE` 或 `PixelFormat.TRANSLUCENT`。
     */
    @Deprecated("Deprecated in Java") // 覆盖了 Java 中已弃用的成员
    override fun getOpacity(): Int {
        // 如果填充画笔完全不透明，则返回 OPAQUE，否则返回 TRANSLUCENT
        return if (paint.alpha == 255) PixelFormat.OPAQUE else PixelFormat.TRANSLUCENT
    }

    /**
     * 设置 Drawable 的颜色过滤器。
     *
     * @param colorFilter 要应用的 `ColorFilter`。
     */
    @Deprecated("Deprecated in Java") // 覆盖了 Java 中已弃用的成员
    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        paint.colorFilter = colorFilter // 设置填充画笔的颜色过滤器
        borderPaint.colorFilter = colorFilter // 设置边框画笔的颜色过滤器
        invalidateSelf() // 请求 Drawable 重绘
    }

    // --- Public Setters for Dynamic Updates ---
    // 以下方法允许在运行时动态修改 Drawable 的样式，而不仅仅通过 XML 属性。

    /**
     * 动态设置背景 Drawable。如果设置，会清除所有颜色、边框和圆角样式。
     *
     * @param drawable 要设置的 Drawable。
     */
    fun setBackgroundDrawable(drawable: Drawable?) {
        // 只有当新的 Drawable 与当前的不同时才更新
        if (this.backgroundDrawable != drawable) {
            this.backgroundDrawable = drawable
            // 清除所有相关的颜色和样式属性
            backgroundColor = Color.TRANSPARENT
            backgroundStartColor = Color.TRANSPARENT
            backgroundCenterColor = Color.TRANSPARENT
            backgroundEndColor = Color.TRANSPARENT
            backgroundGradientAngle = 0f
            borderWidth = 0f
            cornerRadius = 0f
            cornerRadii = null
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置背景渐变颜色。支持两色或三色渐变。
     *
     * @param startColor 渐变起始颜色。
     * @param centerColor 渐变中心颜色 (可选，默认为透明，表示两色渐变)。
     * @param endColor 渐变结束颜色。
     */
    fun setBackgroundColors(@ColorInt startColor: Int, @ColorInt centerColor: Int = Color.TRANSPARENT, @ColorInt endColor: Int) {
        // 只有当没有 Drawable 背景且颜色组合发生变化时才更新
        if (backgroundDrawable == null && (
                    this.backgroundStartColor != startColor ||
                            this.backgroundCenterColor != centerColor ||
                            this.backgroundEndColor != endColor ||
                            this.backgroundColor != Color.TRANSPARENT // 如果之前是纯色背景，也需要强制更新
                    )
        ) {
            this.backgroundStartColor = startColor
            this.backgroundCenterColor = centerColor
            this.backgroundEndColor = endColor
            this.backgroundColor = Color.TRANSPARENT // 设置渐变颜色意味着纯色背景失效
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置纯色背景颜色。会清除任何渐变设置。
     *
     * @param color 背景颜色。
     */
    fun setBackgroundColor(@ColorInt color: Int) {
        // 只有当颜色不同或之前有 Drawable 背景时才更新
        if (this.backgroundColor != color || backgroundStartColor != color || backgroundDrawable != null) {
            this.backgroundColor = color
            // 将所有渐变颜色也设为该纯色，方便统一绘制逻辑
            backgroundStartColor = color
            backgroundCenterColor = color
            backgroundEndColor = color
            backgroundDrawable = null // 清除 Drawable 背景
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置背景渐变角度。
     *
     * @param angle 渐变角度（0-360度）。
     */
    fun setBackgroundGradientAngle(angle: Float) {
        // 只有当角度不同时才更新
        if (this.backgroundGradientAngle != angle) {
            this.backgroundGradientAngle = angle
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置边框宽度。
     *
     * @param width 边框宽度（像素）。
     */
    fun setBorderWidth(width: Float) {
        // 只有当没有 Drawable 背景且宽度不同时才更新
        if (backgroundDrawable == null && this.borderWidth != width) {
            this.borderWidth = width
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置边框渐变颜色。支持两色或三色渐变。
     *
     * @param startColor 渐变起始颜色。
     * @param centerColor 渐变中心颜色 (可选，默认为透明，表示两色渐变)。
     * @param endColor 渐变结束颜色。
     */
    fun setBorderColors(@ColorInt startColor: Int, @ColorInt centerColor: Int = Color.TRANSPARENT, @ColorInt endColor: Int) {
        // 只有当没有 Drawable 背景且颜色组合发生变化时才更新
        if (backgroundDrawable == null && (
                    this.borderStartColor != startColor ||
                            this.borderCenterColor != centerColor ||
                            this.borderEndColor != endColor ||
                            this.borderColor != Color.TRANSPARENT // 如果之前是纯色边框，也需要强制更新
                    )
        ) {
            this.borderStartColor = startColor
            this.borderCenterColor = centerColor
            this.borderEndColor = endColor
            this.borderColor = Color.TRANSPARENT // 设置渐变颜色意味着纯色边框失效
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置纯色边框颜色。会清除任何渐变设置。
     *
     * @param color 边框颜色。
     */
    fun setBorderColor(@ColorInt color: Int) {
        // 只有当没有 Drawable 背景且颜色不同时才更新
        if (backgroundDrawable == null && (this.borderColor != color || borderStartColor != color)) {
            this.borderColor = color
            // 将所有渐变颜色也设为该纯色，方便统一绘制逻辑
            borderStartColor = color
            borderCenterColor = color
            borderEndColor = color
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置边框渐变角度。
     *
     * @param angle 渐变角度（0-360度）。
     */
    fun setBorderGradientAngle(angle: Float) {
        // 只有当没有 Drawable 背景且角度不同时才更新
        if (backgroundDrawable == null && this.borderGradientAngle != angle) {
            this.borderGradientAngle = angle
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置统一的圆角半径。会清除任何单个角落的圆角设置。
     *
     * @param radius 圆角半径（像素）。
     */
    fun setCornerRadius(radius: Float) {
        // 只有当没有 Drawable 背景且圆角半径不同或之前有单独圆角设置时才更新
        if (backgroundDrawable == null && (this.cornerRadius != radius || cornerRadii != null)) {
            this.cornerRadius = radius
            // 设置八个角的半径值，确保统一
            cornerRadii = floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius)
            invalidateSelf() // 请求重绘
        }
    }

    /**
     * 动态设置各个角落的圆角半径。
     *
     * @param topLeft 左上角圆角半径。
     * @param topRight 右上角圆角半径。
     * @param bottomRight 右下角圆角半径。
     * @param bottomLeft 左下角圆角半径。
     */
    fun setCornerRadii(topLeft: Float, topRight: Float, bottomRight: Float, bottomLeft: Float) {
        // 检查圆角值是否有变化
        val changed = cornerRadii == null ||
                cornerRadii!![0] != topLeft || cornerRadii!![2] != topRight ||
                cornerRadii!![4] != bottomRight || cornerRadii!![6] != bottomLeft

        // 只有当没有 Drawable 背景且圆角值有变化时才更新
        if (backgroundDrawable == null && changed) {
            cornerRadius = 0f // 清除统一圆角设置
            // 设置各个角的半径值
            cornerRadii = floatArrayOf(topLeft, topLeft, topRight, topRight,
                bottomRight, bottomRight, bottomLeft, bottomLeft)
            invalidateSelf() // 请求重绘
        }
    }
}