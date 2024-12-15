package com.ww.tools.uichecker.drawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.ww.tools.uichecker.drawer.base.ViewGroupDrawer
import com.ww.tools.uichecker.model.ViewItem
import kotlin.math.abs

class InfoDrawer(context: Context) : ViewGroupDrawer(context) {
    enum class Direction {
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM,
        CENTER
    }

    override fun onDraw(canvas: Canvas, list: List<ViewItem?>) {
        for (viewItem in list) {
            if (viewItem?.visibility == 0) {
                val x1 = viewItem.location.left
                val y1 = viewItem.location.top
                val x2 = viewItem.location.right
                val y2 = viewItem.location.bottom
                val width = abs((x1 - x2).toDouble()).toInt()
                val height = abs((y1 - y2).toDouble()).toInt()
                val size = StringBuilder().append(pixelsToDips(width)).append('x')
                    .append(pixelsToDips(height)).toString()
                drawViewInfo(size, Direction.LEFT_TOP, canvas, viewItem.location, viewItem)
                val backgroundColor = viewItem.backgroundColor
                if (backgroundColor != null) {
                    drawViewInfo(
                        toHexString(backgroundColor),
                        Direction.RIGHT_TOP,
                        canvas,
                        viewItem.location,
                        viewItem
                    )
                }
            }
        }
    }

    private fun drawViewInfo(
        info: String,
        direction: Direction,
        canvas: Canvas,
        location: Rect,
        viewItem: ViewItem?
    ) {
        val x1 = location.left
        val y1 = location.top
        val x2 = location.right
        val y2 = location.bottom
        mPaint.textSize = 12.0f
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.RED
        val fontMetrics = mPaint.fontMetrics
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        val baseline = (fontHeight / 2) - fontMetrics.bottom
        when (direction) {
            Direction.LEFT_TOP -> {
                if (!viewItem!!.hasChildView) {
                    mPaint.textAlign = Paint.Align.LEFT
                    canvas.drawText(
                        info,
                        0,
                        info.length,
                        (x1 + 1).toFloat(),
                        (y1 - fontMetrics.top) + 1,
                        mPaint
                    )
                    return
                }
                return
            }

            Direction.RIGHT_TOP -> {
                if (!viewItem!!.hasChildView) {
                    mPaint.textAlign = Paint.Align.RIGHT
                    canvas.drawText(
                        info,
                        0,
                        info.length,
                        (x2 - 1).toFloat(),
                        (y1 - fontMetrics.top) + 1,
                        mPaint
                    )
                    return
                }
                return
            }

            Direction.LEFT_BOTTOM -> {
                if (!viewItem!!.hasChildView) {
                    mPaint.textAlign = Paint.Align.LEFT
                    canvas.drawText(
                        info,
                        0,
                        info.length,
                        (x1 + 1).toFloat(),
                        (y2 - 1).toFloat(),
                        mPaint
                    )
                    return
                }
                return
            }

            Direction.RIGHT_BOTTOM -> {
                if (!viewItem!!.hasChildView) {
                    mPaint.textAlign = Paint.Align.RIGHT
                    canvas.drawText(
                        info,
                        0,
                        info.length,
                        (x2 - 2).toFloat(),
                        (y2 - 2).toFloat(),
                        mPaint
                    )
                    return
                }
                return
            }

            Direction.CENTER -> {
                val centerX = (x1 + x2) / 2
                val centerY = (y1 + y2) / 2
                if (!viewItem!!.hasChildView) {
                    mPaint.textAlign = Paint.Align.CENTER
                    canvas.drawText(
                        info,
                        0,
                        info.length,
                        centerX.toFloat(),
                        centerY + baseline,
                        mPaint
                    )
                    return
                }
                return
            }

            else -> return
        }
    }
}
