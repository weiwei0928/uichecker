package com.ww.tools.uichecker.widget

import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RippleDrawable
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ww.tools.uichecker.R
import com.ww.tools.uichecker.drawable.ViewDrawable
import com.ww.tools.uichecker.drawable.ViewGroupDrawable
import com.ww.tools.uichecker.utils.RunningSettings
import kotlin.math.abs
import kotlin.math.min

class BorderViewFrameLayout(var activity: Activity) : FrameLayout(activity) {
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var enablePreviewMode = false
    private var scaleView: ViewGroup? = null
    private var enableScaleMode = false
    private var initTouchX = 0
    private var initTouchY = 0
    private var lastTouchX = 0
    private var lastTouchY = 0
    private var downTime: Long = 0
    private val scaleGestureListener = object : ScaleGestureDetector.OnScaleGestureListener {
        private val MAX_SCALE = 15f
        private var initFocusX = 0f
        private var initFocusY = 0f
        private var initScaleX = 0f
        private var initScaleY = 0f

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            val currentScaleX = scaleView?.scaleX ?: return false
            if (currentScaleX >= MAX_SCALE && scale >= 1.0f) {
                return false
            }
            scaleView?.apply {
                pivotX = initFocusX
                pivotY = initFocusY
                scaleX = min(initScaleX + ((scale - 1) * initScaleX * 1.5f), MAX_SCALE)
                scaleY = min(initScaleY + ((scale - 1) * initScaleY * 1.5f), MAX_SCALE)
                translationX = detector.focusX - initFocusX
                translationY = detector.focusY - initFocusY
            }
            return false
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            initScaleX = scaleView?.scaleX ?: return false
            initScaleY = scaleView?.scaleY ?: return false
            val prevFocusX = scaleView?.pivotX ?: return false
            val prevFocusY = scaleView?.pivotY ?: return false
            val currFocusX = (prevFocusX * initScaleX + (detector.focusX - prevFocusX)) - (scaleView?.translationX ?: 0f)
            val currFocusY = (prevFocusY * initScaleY + (detector.focusY - prevFocusY)) - (scaleView?.translationY ?: 0f)
            initFocusX = currFocusX / initScaleX
            initFocusY = currFocusY / initScaleY
            scaleView?.apply {
                translationX = 0f
                translationY = 0f
            }
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {}
    }


    companion object {
        private const val TAG = "BorderViewFrameLayout"

        fun install(activity: Activity) {
            activity.window.decorView.let { decorView ->
                if (decorView is ViewGroup && decorView.getChildAt(0) !is BorderViewFrameLayout) {
                    val borderViewFrameLayout = BorderViewFrameLayout(activity).apply {
                        id = R.id.border_view
                    }
                    while (decorView.childCount > 0) {
                        val childView = decorView.getChildAt(0)
                        decorView.removeView(childView)
                        borderViewFrameLayout.addView(childView)
                    }
                    decorView.addView(
                        borderViewFrameLayout,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            }
        }

        fun isInstalled(activity: Activity): Boolean {
            return activity.window?.decorView?.let {
                it is ViewGroup && it.getChildAt(0) is BorderViewFrameLayout
            } ?: false
        }

        fun uninstalled(activity: Activity) {
            activity.window?.decorView?.let { decorView ->
                if (decorView is ViewGroup) {
                    val borderView = decorView.getChildAt(0)
                    if (borderView is BorderViewFrameLayout) {
                        borderView.updateChildrenForeground()
                        while (borderView.childCount > 0) {
                            val childView = borderView.getChildAt(0)
                            borderView.removeView(childView)
                            decorView.addView(childView)
                        }
                        decorView.removeView(borderView)
                    }
                }
            }
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        this.scaleGestureDetector = ScaleGestureDetector(context, this.scaleGestureListener)
        val window = activity.window
        val decorView = window.decorView
        this.scaleView = decorView as ViewGroup
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateChildrenForeground()
    }

    fun updateChildrenForeground() {
        if (RunningSettings.isShowLayoutBorder()) {
            replaceChildrenForeground(this)
        } else {
            clearChildrenForeground(this)
        }
    }

    private fun replaceChildrenForeground(rootView: View) {
        if ((rootView is ViewGroup) && rootView.childCount > 0) {
            replaceChildForeground(rootView, ViewGroupDrawable(rootView))
            val childCount = rootView.childCount
            for (index in 0 until childCount) {
                val childAt = rootView.getChildAt(index)
                replaceChildrenForeground(childAt)
            }
            return
        }
        replaceChildForeground(rootView, ViewDrawable(rootView))
    }

    private fun replaceChildForeground(view: View, newDrawable: Drawable) {
        val layerDrawable: LayerDrawable
        val layerDrawable2: LayerDrawable
        val oldDrawable = view.foreground
        if (oldDrawable != null) {
            when (oldDrawable) {
                is RippleDrawable -> {
                    layerDrawable2 = oldDrawable
                }

                is LayerDrawable -> {
                    val lastDrawable = if (oldDrawable.numberOfLayers > 0) oldDrawable.getDrawable(
                        oldDrawable.numberOfLayers - 1
                    ) else null
                    if (lastDrawable != null && ((lastDrawable is ViewDrawable) || (lastDrawable is ViewGroupDrawable))) {
                        return
                    }
                    oldDrawable.addLayer(newDrawable)
                    layerDrawable2 = oldDrawable
                }

                else -> {
                    layerDrawable2 = LayerDrawable(arrayOf(oldDrawable, newDrawable))
                }
            }
            layerDrawable = layerDrawable2
        } else {
            layerDrawable = LayerDrawable(arrayOf(newDrawable))
        }
        view.foreground = layerDrawable
    }

    private fun clearChildrenForeground(view: View) {
        if (view is ViewGroup) {
            clearChildForeground(view)
            val childCount = view.childCount
            for (index in 0 until childCount) {
                val childAt = view.getChildAt(index)
                clearChildrenForeground(childAt)
            }
            return
        }
        clearChildForeground(view)
    }

    private fun clearChildForeground(view: View) {
        val foreground = view.foreground
        if (foreground == null || foreground !is LayerDrawable) {
            return
        }
        val drawables: MutableList<Drawable?> = ArrayList()
        val numberOfLayers = foreground.numberOfLayers
        for (i in 0 until numberOfLayers) {
            val drawable = foreground.getDrawable(i)
            if (drawable !is ViewDrawable && drawable !is ViewGroupDrawable) {
                drawables.add(drawable)
            }
        }

        val array = drawables.toTypedArray()
        view.foreground = LayerDrawable(array)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action and 255) {
            0 -> {
                this.initTouchX = x
                this.initTouchY = y
                this.downTime = System.currentTimeMillis()
            }

            1 -> {
                if (this.enableScaleMode) {
                    this.enableScaleMode = false
                }
                if (this.enablePreviewMode && (System.currentTimeMillis() - this.downTime < 500) && (abs(
                        (x - this.initTouchX).toDouble()
                    ) < 10) && (abs((y - this.initTouchY).toDouble()) < 10)
                ) {
                    this.enablePreviewMode = false
                    val viewGroup = this.scaleView
                    viewGroup!!.scaleX = 1.0f
                    val viewGroup2 = this.scaleView
                    viewGroup2!!.scaleY = 1.0f
                    val viewGroup3 = this.scaleView
                    viewGroup3!!.translationX = 0.0f
                    val viewGroup4 = this.scaleView
                    viewGroup4!!.translationY = 0.0f
                    return true
                }
            }

            2 -> if (this.enablePreviewMode && !this.enableScaleMode) {
                val viewGroup5 = this.scaleView
                viewGroup5!!.translationX =
                    viewGroup5.translationX + (x - this.lastTouchX)
                val viewGroup6 = this.scaleView
                viewGroup6!!.translationY =
                    viewGroup6.translationY + (y - this.lastTouchY)
            }

            5 -> {
                this.enablePreviewMode = true
                this.enableScaleMode = true
            }
        }
        this.lastTouchX = x
        this.lastTouchY = y
        if (!this.enablePreviewMode) {
            super.dispatchTouchEvent(event)
        }
        val scaleGestureDetector = this.scaleGestureDetector
        return scaleGestureDetector!!.onTouchEvent(event)
    }

}
