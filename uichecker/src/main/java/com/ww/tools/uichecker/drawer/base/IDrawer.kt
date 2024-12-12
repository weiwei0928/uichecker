package com.ww.tools.uichecker.drawer.base

import android.content.Context
import android.graphics.Paint
import java.util.Locale

abstract class IDrawer(val context: Context) {
    val mPaint: Paint = Paint()

    fun dipsToPixels(dips: Int): Int {
        val resources = context.resources
        val scale = resources.displayMetrics.density
        return ((dips * scale) + 0.5f).toInt()
    }

    fun pixelsToDips(pixels: Int): Int {
        val resources = context.resources
        val scale = resources.displayMetrics.density
        return (pixels / scale).toInt()
    }

    fun pixelsToScaleDips(pixels: Float): Int {
        val resources = context.resources
        val scale = resources.displayMetrics.scaledDensity
        return (pixels / scale).toInt()
    }

    fun sign(x: Int): Int {
        return if (x >= 0) 1 else -1
    }

    fun getTextSize(text: String, distance: Float): Float {
        val charWidth = (distance / text.length).toInt()
        return charWidth.coerceIn(dipsToPixels(5), dipsToPixels(10)).toFloat()
    }

    fun toHexString(value: Int): String {
        val hexString = Integer.toHexString(value)
        val locale = Locale.US
        return hexString.uppercase(locale)
    }
}
