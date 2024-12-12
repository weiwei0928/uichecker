package com.ww.tools.uichecker.utils

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.util.Locale
import kotlin.math.abs

object DrawUtils {

    const val DEBUG_CORNERS_SIZE_DIP: Int = 8
    val DEBUG_CORNERS_COLOR: Int = Color.rgb(63, 127, 255)
    private val sDebugLines = FloatArray(12)
    private val s8Points = FloatArray(16)
    fun drawDistance(canvas: Canvas, paint: Paint, fromX: Int, fromY: Int, toX: Int, toY: Int) {
        val distance =
            if (fromY == toY) abs((toX - fromX).toDouble()).toInt() else abs((toY - fromY).toDouble())
                .toInt()
        val dpValue = pixelsToDips(distance)
        if (dpValue <= 0 || dpValue > 150) {
            return
        }
        val dpText = dpValue.toString()
        paint.style = Paint.Style.FILL
        paint.textSize = getTextSize(dpText, distance.toFloat())
        paint.color = -65536
        paint.strokeWidth = 1.0f
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
            canvas.drawLines(sDebugLines, paint)
            paint.textAlign = Paint.Align.CENTER
            val x = (fromX + toX) / 2
            val y = fromY - 1
            canvas.drawText(dpText, 0, dpText.length, x.toFloat(), y.toFloat(), paint)
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
            canvas.drawLines(sDebugLines, paint)
            paint.textAlign = Paint.Align.LEFT
            val fontMetrics = paint.fontMetrics
            val x2 = fromX + 1
            val y2 =
                (((fromY + toY) / 2) + ((fontMetrics.bottom - fontMetrics.top) / 2)) - fontMetrics.bottom
            canvas.drawText(dpText, 0, dpText.length, x2.toFloat(), y2, paint)
        }
    }

    fun drawRectCorners(canvas: Canvas, paint: Paint, x1: Int, y1: Int, x2: Int, y2: Int) {
        paint.style = Paint.Style.FILL
        val lineLength = dipsToPixels(DEBUG_CORNERS_SIZE_DIP)
        val lineWidth = dipsToPixels(1)
        drawCorner(canvas, paint, x1, y1, lineLength, lineLength, lineWidth)
        drawCorner(canvas, paint, x1, y2, lineLength, -lineLength, lineWidth)
        drawCorner(canvas, paint, x2, y1, -lineLength, lineLength, lineWidth)
        drawCorner(canvas, paint, x2, y2, -lineLength, -lineLength, lineWidth)
    }

    private fun drawRect(canvas: Canvas, paint: Paint, x1: Int, y1: Int, x2: Int, y2: Int) {
        s8Points[0] = x1.toFloat()
        s8Points[1] = y1.toFloat()
        s8Points[2] = x2.toFloat()
        s8Points[3] = y1.toFloat()
        s8Points[4] = x2.toFloat()
        s8Points[5] = y1.toFloat()
        s8Points[6] = x2.toFloat()
        s8Points[7] = y2.toFloat()
        s8Points[8] = x2.toFloat()
        s8Points[9] = y2.toFloat()
        s8Points[10] = x1.toFloat()
        s8Points[11] = y2.toFloat()
        s8Points[12] = x1.toFloat()
        s8Points[13] = y2.toFloat()
        s8Points[14] = x1.toFloat()
        s8Points[15] = y1.toFloat()
        canvas.drawLines(s8Points, paint)
    }

    private fun drawCorner(c: Canvas, paint: Paint, x1: Int, y1: Int, dx: Int, dy: Int, lw: Int) {
        fillRect(c, paint, x1, y1, x1 + dx, y1 + (lw * sign(dy)))
        fillRect(c, paint, x1, y1, x1 + (lw * sign(dx)), y1 + dy)
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

    private fun dipsToPixels(dips: Int): Int {
        val resources: Resources = ApplicationUtils.context.resources
        val scale = resources.displayMetrics.density
        return ((dips * scale) + 0.5f).toInt()
    }

    private fun pixelsToDips(pixels: Int): Int {
        val resources: Resources = ApplicationUtils.context.resources
        val scale = resources.displayMetrics.density
        return (pixels / scale).toInt()
    }

    fun pixelsToScaleDips(pixels: Float): Int {
        val resources: Resources = ApplicationUtils.context.resources
        val scale = resources.displayMetrics.scaledDensity
        return (pixels / scale).toInt()
    }

    private fun sign(x: Int): Int {
        return if (x >= 0) 1 else -1
    }

    private fun getTextSize(text: String, distance: Float): Float {
        val charWidth = (distance / text.length).toInt()
        return charWidth.coerceIn(dipsToPixels(5), dipsToPixels(10)).toFloat()
    }

    fun toHexString(value: Int): String {
        val hexString = Integer.toHexString(value)
        val locale = Locale.US
        val upperCase = hexString.uppercase(locale)
        return upperCase
    }

}
