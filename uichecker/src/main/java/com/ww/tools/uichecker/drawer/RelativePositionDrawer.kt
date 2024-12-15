package com.ww.tools.uichecker.drawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.ww.tools.uichecker.drawer.base.ViewGroupDrawer
import com.ww.tools.uichecker.model.ViewItem
import kotlin.math.abs

class RelativePositionDrawer(context: Context) : ViewGroupDrawer(context) {
    private val sDebugLines = FloatArray(12)

    override fun onDraw(canvas: Canvas, list: List<ViewItem?>) {
        for (viewItem in list) {
            if (viewItem!!.visibility == 0) {
                val leftViewItem = viewItem.leftViewItem
                if (leftViewItem != null) {
                    if (leftViewItem.rightViewItem == null || viewItem != leftViewItem.rightViewItem) {
                        val fromX = viewItem.location.left
                        val fromY = viewItem.location.top + (viewItem.location.height() / 2)
                        val toX = leftViewItem.location.right
                        drawLine(canvas, fromX, fromY, toX, fromY)
                    }
                } else {
                    val parentViewItem = viewItem.parentViewItem
                    if (parentViewItem != null) {
                        val fromX2 = parentViewItem.location.left
                        val fromY2 = viewItem.location.top + (viewItem.location.height() / 2)
                        val toX2 = viewItem.location.left
                        drawLine(canvas, fromX2, fromY2, toX2, fromY2)
                    }
                }
                val topViewItem = viewItem.topViewItem
                if (topViewItem != null) {
                    if (topViewItem.bottomViewItem == null || viewItem != topViewItem.bottomViewItem) {
                        val fromX3 = viewItem.location.left + (viewItem.location.width() / 2)
                        val fromY3 = viewItem.location.top
                        val toY = topViewItem.location.bottom
                        drawLine(canvas, fromX3, fromY3, fromX3, toY)
                    }
                } else {
                    val parentViewItem2 = viewItem.parentViewItem
                    if (parentViewItem2 != null) {
                        val fromX4 = viewItem.location.left + (viewItem.location.width() / 2)
                        val fromY4 = parentViewItem2.location.top
                        val toY2 = viewItem.location.top
                        drawLine(canvas, fromX4, fromY4, fromX4, toY2)
                    }
                }
                val rightViewItem = viewItem.rightViewItem
                if (rightViewItem != null) {
                    val fromX5 = viewItem.location.right
                    val fromY5 = viewItem.location.top + (viewItem.location.height() / 2)
                    val toX3 = rightViewItem.location.left
                    drawLine(canvas, fromX5, fromY5, toX3, fromY5)
                } else {
                    val parentViewItem3 = viewItem.parentViewItem
                    if (parentViewItem3 != null) {
                        val fromX6 = viewItem.location.right
                        val fromY6 = viewItem.location.top + (viewItem.location.height() / 2)
                        val toX4 = parentViewItem3.location.right
                        drawLine(canvas, fromX6, fromY6, toX4, fromY6)
                    }
                }
                val bottomViewItem = viewItem.bottomViewItem
                if (bottomViewItem != null) {
                    val fromX7 = viewItem.location.left + (viewItem.location.width() / 2)
                    val fromY7 = viewItem.location.bottom
                    val toY3 = bottomViewItem.location.top
                    drawLine(canvas, fromX7, fromY7, fromX7, toY3)
                } else {
                    val parentViewItem4 = viewItem.parentViewItem
                    if (parentViewItem4 != null) {
                        val fromX8 = viewItem.location.left + (viewItem.location.width() / 2)
                        val fromY8 = viewItem.location.bottom
                        val toY4 = parentViewItem4.location.bottom
                        drawLine(canvas, fromX8, fromY8, fromX8, toY4)
                    }
                }
            }
        }
    }

    private fun drawLine(canvas: Canvas, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        val distance =
            if (fromY == toY) abs((toX - fromX).toDouble()).toInt() else abs((toY - fromY).toDouble())
                .toInt()
        val dpValue = pixelsToDips(distance)
        if (dpValue <= 0 || dpValue > 150) {
            return
        }
        val dpText = dpValue.toString()
        mPaint.textSize = getTextSize(dpText, distance.toFloat())
        mPaint.color = Color.GREEN
        mPaint.strokeWidth = 1.0f
        val lineLabelLength = dipsToPixels(3)
        if (fromY == toY) {
            sDebugLines[0] = fromX.toFloat()
            sDebugLines[1] = fromY.toFloat()
            sDebugLines[2] = toX.toFloat()
            sDebugLines[3] = toY.toFloat()
            sDebugLines[4] = fromX.toFloat()
            sDebugLines[5] = (fromY - lineLabelLength).toFloat()
            sDebugLines[6] = fromX.toFloat()
            sDebugLines[7] = (fromY + lineLabelLength).toFloat()
            sDebugLines[8] = toX.toFloat()
            sDebugLines[9] = (toY - lineLabelLength).toFloat()
            sDebugLines[10] = toX.toFloat()
            sDebugLines[11] = (toY + lineLabelLength).toFloat()
            canvas.drawLines(this.sDebugLines, mPaint)
            mPaint.textAlign = Paint.Align.CENTER
            val x = (fromX + toX) / 2
            val y = fromY - 1
            canvas.drawText(dpText, 0, dpText.length, x.toFloat(), y.toFloat(), mPaint)
        } else if (fromX == toX) {
            sDebugLines[0] = fromX.toFloat()
            sDebugLines[1] = fromY.toFloat()
            sDebugLines[2] = toX.toFloat()
            sDebugLines[3] = toY.toFloat()
            sDebugLines[4] = (fromX - lineLabelLength).toFloat()
            sDebugLines[5] = fromY.toFloat()
            sDebugLines[6] = (fromX + lineLabelLength).toFloat()
            sDebugLines[7] = fromY.toFloat()
            sDebugLines[8] = (toX - lineLabelLength).toFloat()
            sDebugLines[9] = toY.toFloat()
            sDebugLines[10] = (toX + lineLabelLength).toFloat()
            sDebugLines[11] = toY.toFloat()
            canvas.drawLines(this.sDebugLines, mPaint)
            mPaint.textAlign = Paint.Align.LEFT
            val fontMetrics = mPaint.fontMetrics
            val x2 = fromX + 1
            val y2 =
                (((fromY + toY) / 2) + ((fontMetrics.bottom - fontMetrics.top) / 2)) - fontMetrics.bottom
            canvas.drawText(dpText, 0, dpText.length, x2.toFloat(), y2, mPaint)
        }
    }
}
