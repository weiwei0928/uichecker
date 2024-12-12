package com.ww.tools.uichecker.drawer.base

import android.content.Context
import android.graphics.Canvas
import android.view.View

abstract class ViewDrawer<T : View>(context: Context) : IDrawer(context) {
    abstract fun onDraw(canvas: Canvas, t: T)
}
