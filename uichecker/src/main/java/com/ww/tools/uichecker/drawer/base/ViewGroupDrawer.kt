package com.ww.tools.uichecker.drawer.base

import android.content.Context
import android.graphics.Canvas
import com.ww.tools.uichecker.model.ViewItem

abstract class ViewGroupDrawer(context: Context) : IDrawer(context) {
    abstract fun onDraw(canvas: Canvas, list: List<ViewItem?>)
}
