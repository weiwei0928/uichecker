package com.ww.tools.uichecker.floatview

import android.app.Activity
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.ww.tools.uichecker.utils.FloatUtils

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

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: androidx.lifecycle.LifecycleOwner, event: androidx.lifecycle.Lifecycle.Event) {
                when (event) {
                    androidx.lifecycle.Lifecycle.Event.ON_START -> floatView?.onStart()
                    androidx.lifecycle.Lifecycle.Event.ON_STOP -> floatView?.onStop()
                    else -> {}
                }
            }
        })

    }
}