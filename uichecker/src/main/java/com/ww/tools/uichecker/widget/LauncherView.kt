package com.ww.tools.uichecker.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.customview.widget.ViewDragHelper
import com.ww.tools.uichecker.R
import com.ww.tools.uichecker.utils.SpUtil
import com.ww.tools.uichecker.utils.SpUtil.isShowLayoutBorder
import kotlin.math.max
import kotlin.math.min

class LauncherView(private var activity: Activity) : FrameLayout(activity) {

    private var logoView: ImageView
//    private var rootView: View
//    private var switch: Switch?
    private var viewDragHelper: ViewDragHelper
    private var isChecked = false

    init {
        inflate(context, R.layout.lanucher_logo, this)
//        inflate(context, R.layout.light_float_item, this)
//        rootView = findViewById(R.id.root_view)
//        switch = rootView.findViewById(R.id.float_switch)

        logoView = findViewById(R.id.logo)
        logoView.drawable.setTint(Color.parseColor("#FFFF8400"))
        setPadding(0, getStatusBarHeight(activity), 0, 0)

        viewDragHelper = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child === rootView
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                val leftBound = paddingLeft
                val rightBound = width - rootView.measuredWidth
                return min(max(left, leftBound), rightBound)
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                val topBound = paddingTop
                val bottomBound = height - rootView.measuredHeight - paddingTop
                return min(max(top, topBound), bottomBound)
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return width - rootView.measuredWidth
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return height - rootView.measuredHeight
            }
        })

//        switch?.isChecked = SpUtil.isShowLayoutBorder()
//        switch?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//
//            SpUtil.setShowLayoutBorder(isChecked)
//            if (isChecked) {
//                if (!BorderViewFrameLayout.isInstalled(switch?.context as Activity)) {
//                    BorderViewFrameLayout.install(switch?.context as Activity)
//                    return@OnCheckedChangeListener
//                }
//                return@OnCheckedChangeListener
//            }
//            BorderViewFrameLayout.uninstalled(switch?.context as Activity)
//        })
        logoView.setOnClickListener {
            isChecked = !isChecked
            SpUtil.setShowLayoutBorder(isChecked)
            if (isChecked) {
                if (!BorderViewFrameLayout.isInstalled(activity)) {
                    BorderViewFrameLayout.install(activity)
                    return@setOnClickListener
                }
                return@setOnClickListener
            }

            BorderViewFrameLayout.uninstalled(activity)


        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return viewDragHelper.shouldInterceptTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }

    private fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    companion object {
        fun install(activity: Activity) {
            val decorView = activity.window.decorView as ViewGroup
            decorView.addView(
                LauncherView(activity),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            if (isShowLayoutBorder()) {
                BorderViewFrameLayout.install(activity)
            }
        }
    }
}