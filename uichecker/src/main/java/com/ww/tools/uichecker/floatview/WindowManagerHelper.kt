package com.ww.tools.uichecker.floatview

import android.app.Service.WINDOW_SERVICE
import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import com.ww.tools.uichecker.utils.FloatUtils

class WindowManagerHelper(var context: Context) {

    private var windowManager: WindowManager? = null
    var layoutParams: WindowManager.LayoutParams? = null
        private set

    init {
        initParams()
    }

    private fun initParams() {
        windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            //位置大小设置
            width = WRAP_CONTENT
            height = WRAP_CONTENT
            gravity = Gravity.LEFT or Gravity.TOP
            //设置初始屏幕显示
            x = FloatUtils.FLOAT_VIEW_INIT_X
            y = context.resources.displayMetrics.heightPixels / 2 - 120
        }
    }

    fun addView(floatView: View) {
        windowManager?.addView(floatView, layoutParams)
    }

    fun remove(floatView: View) {
        windowManager?.removeView(floatView)
    }

    fun updateLayout(floatView: View) {
        //更新悬浮控件位置
        windowManager?.updateViewLayout(floatView, layoutParams)
    }


}