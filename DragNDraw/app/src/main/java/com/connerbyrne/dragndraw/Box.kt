package com.connerbyrne.dragndraw

import android.graphics.PointF
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max

data class Box(val start: PointF) {
    var end: PointF = start

    val width: Float
        get() = abs(start.x - end.x)
    val height: Float
        get() = abs(start.y - end.y)

    val left: Float
        get() = min(start.x, end.x)

    val right: Float
        get() = max(start.x, end.x)

    val top: Float
        get() = min(start.y, end.y)

    val bottom: Float
        get() = max(start.y, end.y)
}
