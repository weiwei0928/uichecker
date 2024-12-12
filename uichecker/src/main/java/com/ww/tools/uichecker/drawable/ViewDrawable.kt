package com.ww.tools.uichecker.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.ww.tools.uichecker.drawer.TextViewDrawer

class ViewDrawable(val view: View) : Drawable() {
    private var textViewDrawer: TextViewDrawer? = null

    init {
        if (this.view is TextView) {
            val context = this.view.context
            this.textViewDrawer = TextViewDrawer(context)
        }
    }

    override fun draw(canvas: Canvas) {
        if (view is TextView) {
            textViewDrawer?.onDraw(canvas, view)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}
