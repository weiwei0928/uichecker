package com.ww.tools.uichecker.drawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.ww.tools.uichecker.drawer.InfoDrawer.Direction
import com.ww.tools.uichecker.drawer.base.ViewGroupDrawer
import com.ww.tools.uichecker.model.ViewItem
import com.ww.tools.uichecker.utils.DrawUtils

class OutLineDrawer(context: Context) : ViewGroupDrawer(context) {
    private val sDebugLines = FloatArray(16)

    companion object {
        private const val DEBUG_CORNERS_SIZE_DIP = 8f
        private val DEBUG_CORNERS_COLOR = Color.rgb(63, 127, 255)
    }

    override fun onDraw(canvas: Canvas, list: List<ViewItem?>) {
        for (viewItem in list) {
            if (viewItem!!.visibility == 0) {
                val paint = mPaint
                paint.color = DEBUG_CORNERS_COLOR
                paint.style = Paint.Style.STROKE
                drawRect(
                    canvas,
                    paint,
                    viewItem.location.left,
                    viewItem.location.top,
                    viewItem.location.right,
                    viewItem.location.bottom
                )
                paint.color = DEBUG_CORNERS_COLOR
                paint.style = Paint.Style.FILL
                dipsToPixels(8)
                dipsToPixels(1)
                DrawUtils.drawRectCorners(
                    context,
                    canvas,
                    paint,
                    viewItem.location.left,
                    viewItem.location.top,
                    viewItem.location.right,
                    viewItem.location.bottom
                )
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

    private fun drawRect(canvas: Canvas, paint: Paint, x1: Int, y1: Int, x2: Int, y2: Int) {
        sDebugLines[0] = x1.toFloat()
        sDebugLines[1] = y1.toFloat()
        sDebugLines[2] = x2.toFloat()
        sDebugLines[3] = y1.toFloat()
        sDebugLines[4] = x2.toFloat()
        sDebugLines[5] = y1.toFloat()
        sDebugLines[6] = x2.toFloat()
        sDebugLines[7] = y2.toFloat()
        sDebugLines[8] = x2.toFloat()
        sDebugLines[9] = y2.toFloat()
        sDebugLines[10] = x1.toFloat()
        sDebugLines[11] = y2.toFloat()
        sDebugLines[12] = x1.toFloat()
        sDebugLines[13] = y2.toFloat()
        sDebugLines[14] = x1.toFloat()
        sDebugLines[15] = y1.toFloat()
        canvas.drawLines(this.sDebugLines, paint)
    }

    private fun drawMargins(canvas: Canvas, paint: Paint, viewItem: ViewItem) {
        fillDifference(
            canvas,
            viewItem.location.left,
            viewItem.location.top,
            viewItem.location.right,
            viewItem.location.bottom,
            viewItem.leftMargin,
            viewItem.topMargin,
            viewItem.rightMargin,
            viewItem.bottomMargin,
            paint
        )
    }

    private fun fillDifference(
        canvas: Canvas,
        x2: Int,
        y2: Int,
        x3: Int,
        y3: Int,
        dx1: Int,
        dy1: Int,
        dx2: Int,
        dy2: Int,
        paint: Paint
    ) {
        val x1 = x2 - dx1
        val y1 = y2 - dy1
        val x4 = x3 + dx2
        val y4 = y3 + dy2
        fillRect(canvas, paint, x1, y1, x4, y2)
        fillRect(canvas, paint, x1, y2, x2, y3)
        fillRect(canvas, paint, x3, y2, x4, y3)
        fillRect(canvas, paint, x1, y3, x4, y4)
    }

    private fun fillRect(canvas: Canvas, paint: Paint, x1: Int, y1: Int, x2: Int, y2: Int) {
        var x12 = x1
        var y12 = y1
        var x22 = x2
        var y22 = y2
        if (x12 != x22 && y12 != y22) {
            if (x12 > x22) {
                x12 = x22
                x22 = x12
            }
            if (y12 > y22) {
                y12 = y22
                y22 = y12
            }
            canvas.drawRect(x12.toFloat(), y12.toFloat(), x22.toFloat(), y22.toFloat(), paint)
        }
    }

}
