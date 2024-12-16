package com.ww.tools.uichecker.floatview

import android.app.Activity

object FloatViewHelper {

    private var floatView: FloatView? = null
    private lateinit var windowManagerHelper: WindowManagerHelper


    fun init(context: Activity) {
        if (floatView != null) {
            return
        }
        FloatUtils.checkWindowPermission(context) {
            windowManagerHelper = WindowManagerHelper(context)
            floatView = FloatView(context, windowManagerHelper)
            windowManagerHelper.addView(floatView!!.getView())

        }

    }
}