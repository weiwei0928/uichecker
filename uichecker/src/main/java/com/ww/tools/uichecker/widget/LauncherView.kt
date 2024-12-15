package com.ww.tools.uichecker.widget

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat
import androidx.customview.widget.ViewDragHelper
import com.ww.tools.uichecker.R
import com.ww.tools.uichecker.dialog.base.YOYODialog
import com.ww.tools.uichecker.dialog.views.DeveloperOptionsDialog
import com.ww.tools.uichecker.utils.RunningSettings
import com.ww.tools.uichecker.utils.RunningSettings.isShowLayoutBorder
import kotlin.math.max
import kotlin.math.min

class LauncherView(private var activity: Activity) : FrameLayout(activity) {

    private var logoView: ImageView
    private var viewDragHelper: ViewDragHelper
    private var isChecked = false

    init {
        inflate(context, R.layout.lanucher_logo, this)
        logoView = findViewById(R.id.logo)
        logoView.drawable.setTint(Color.parseColor("#FFFF8400"))
        setPadding(0, getStatusBarHeight(activity), 0, 0)

        viewDragHelper = ViewDragHelper.create(this, 1.0f, object : ViewDragHelper.Callback() {
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return child === logoView
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                val leftBound = paddingLeft
                val rightBound = width - logoView.measuredWidth
                return min(max(left, leftBound), rightBound)
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                val topBound = paddingTop
                val bottomBound = height - logoView.measuredHeight - paddingTop
                return min(max(top, topBound), bottomBound)
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return width - logoView.measuredWidth
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return height - logoView.measuredHeight
            }
        })

//        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
//
//            RunningSettings.setShowLayoutBorder(isChecked)
//            if (isChecked) {
//                if (!BorderViewFrameLayout.isInstalled(activity)) {
//                    BorderViewFrameLayout.install(activity)
//                    return@OnCheckedChangeListener
//                }
//                return@OnCheckedChangeListener
//            }
//            BorderViewFrameLayout.uninstalled(activity)
//        })

        logoView.setOnClickListener {
            YOYODialog.Builder(context)
                .setContentView(DeveloperOptionsDialog(activity))
                .setCancelable(true)
                .setNavigationBarColor(Color.GREEN)
                .setDialogStyle(YOYODialog.DialogStyle.BOTTOM)
                .setOutsideTouchable(false)
                .setSlidingDismiss(true)
                .build()
                .show()
//            isChecked = !isChecked
//            RunningSettings.setShowLayoutBorder(isChecked)
//            if (isChecked) {
//                if (!BorderViewFrameLayout.isInstalled(activity)) {
//                    BorderViewFrameLayout.install(activity)
//                    return@setOnClickListener
//                }
//                return@setOnClickListener
//            }
//
//            BorderViewFrameLayout.uninstalled(activity)
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