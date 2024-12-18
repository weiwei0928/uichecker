package com.ww.tools.uichecker.floatview

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.ww.tools.uichecker.utils.FloatUtils
import com.ww.tools.uichecker.utils.SpUtil.isShowLayoutBorder
import com.ww.tools.uichecker.widget.BorderViewFrameLayout

object FloatViewHelper {

    private var floatView: FloatView? = null
    private lateinit var windowManagerHelper: WindowManagerHelper
    private var lifecycleObserver: LifecycleEventObserver? = null

    fun init(activity: Activity) {
        if (floatView != null) {
            uninstall()
        }
        if (isShowLayoutBorder()) {
            BorderViewFrameLayout.install(activity)
        }
        FloatUtils.checkWindowPermission(activity) {
            windowManagerHelper = WindowManagerHelper(activity)
            floatView = FloatView(activity, windowManagerHelper)
            windowManagerHelper.addView(floatView!!.getView())

            // 添加生命周期观察者
            lifecycleObserver = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        floatView?.onStart()
//                        floatView?.hidden()
                    }

                    Lifecycle.Event.ON_STOP -> {
                        floatView?.onStop()
//                        floatView?.show()
                    }

                    Lifecycle.Event.ON_DESTROY -> {
//                            uninstall()
                    }

                    else -> {}
                }
            }
            ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver!!)
        }
    }

    /**
     * 卸载 FloatView，从窗口移除视图并清理资源。
     */
    private fun uninstall() {
        floatView?.let { view ->
            windowManagerHelper.remove(view.getView())
            lifecycleObserver?.let {
                ProcessLifecycleOwner.get().lifecycle.removeObserver(it)
                lifecycleObserver = null
            }
            floatView = null
        }
    }
}