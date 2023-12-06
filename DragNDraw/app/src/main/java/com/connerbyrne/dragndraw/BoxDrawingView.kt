package com.connerbyrne.dragndraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

private const val TAG = "BoxDrawingView"
private val background_color = Color.rgb(248, 239, 224)
private val foreground_color = Color.argb(32, 255, 0, 0)

class BoxDrawingView(
    context: Context,
    attrs: AttributeSet? = null
): View(context, attrs) {
    private var currentBox : Box? = null
    private val boxes = mutableListOf<Box>()

    private var drawCount = 0
    private val maxDrawCount = 3
    
    private val boxPaint = Paint().apply {
        color = 0x22ff0000
        color = foreground_color
    }

    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
        color = background_color
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(backgroundPaint)
        boxes.forEach { box ->
            // Draw square or circle based on width and height
            var left = box.left;
            var top = box.top;
            var right = box.right;
            var bottom = box.bottom;

            if (box.end.x > box.start.x) {
                left = box.start.x
                right = left + Math.min(box.height, box.width)
            } else {
                right = box.start.x
                left = right - Math.min(box.height, box.width)
            }
            if (box.end.y > box.start.y) {
                top = box.start.y
                bottom = top + Math.min(box.height, box.width)
            } else {
                bottom = box.start.y
                top = bottom - Math.min(box.height, box.width)
            }
            if (box.width > box.height) {
                canvas.drawRect(left, top, right, bottom, boxPaint)
            }
            else {
                canvas.drawOval(left, top, right, bottom, boxPaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (drawCount >= maxDrawCount) {
            return true
        }

        val current = PointF(event.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                currentBox = Box(current).also {
                    boxes.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                drawCount++;
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
            }
        }
        return true
    }

    private fun updateCurrentBox(current : PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }
    
}