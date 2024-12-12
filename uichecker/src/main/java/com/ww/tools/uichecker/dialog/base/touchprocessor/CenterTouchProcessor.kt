package com.ww.tools.uichecker.dialog.base.touchprocessor

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.graphics.Rect
import android.view.MotionEvent
import androidx.core.view.ViewCompat
import com.ww.tools.uichecker.dialog.base.YOYODialog
import kotlin.math.max
import kotlin.math.min

class CenterTouchProcessor : TouchProcessor() {
    private var lastMotionEvent: MotionEvent? = null
    private var hasContentTouched = false
    private var downTime: Long = 0

    override fun doProcess(dialog: YOYODialog, event: MotionEvent) {
        val originalRect = dialog.contentRect
        val contentView = dialog.contentView
        when (event.action) {
            0 -> {
                this.hasContentTouched = originalRect.contains(event.x.toInt(), event.y.toInt())
                this.downTime = System.currentTimeMillis()
            }

            1 -> if (this.hasContentTouched) {
                val currRect = Rect()
                contentView.getHitRect(currRect)
                val deltaX = originalRect.left - currRect.left
                val deltaY = originalRect.top - currRect.top
                val slope = (deltaX * 1.0f) / deltaY
                val deltaTime = System.currentTimeMillis() - this.downTime
                val slideSpeed = ((-deltaY) * 1.0f) / (deltaTime.toFloat())
                if (deltaY < (-currRect.height()) / 2 || slideSpeed > 1.0f) {
                    smoothDismiss(
                        dialog,
                        (dialog.window!!.decorView.height - currRect.top) + 50,
                        slope
                    )
                } else {
                    smoothBack(dialog, deltaY, slope)
                }
            }

            2 -> if (this.hasContentTouched) {
                val deltaX2 = event.x - lastMotionEvent!!.x
                val deltaY2 = event.y - lastMotionEvent!!.y
                ViewCompat.offsetTopAndBottom(contentView, deltaY2.toInt())
                ViewCompat.offsetLeftAndRight(contentView, deltaX2.toInt())
                val distance = originalRect.height()
                val scrollDistance = max(
                    ((contentView.top + deltaY2) - originalRect.top).toDouble(),
                    0.0
                ).toFloat()
                val alpha = ((1.0f - min(
                    1.0,
                    (scrollDistance / distance).toDouble()
                )) * dialog.defaultDimAmount).toFloat()
                dialog.window!!.setDimAmount(alpha)
            }
        }
        this.lastMotionEvent = MotionEvent.obtain(event)
    }

    private fun smoothDismiss(dialog: YOYODialog, offsetY: Int, slope: Float) {
        val contentView = dialog.contentView
        val originalRect = dialog.contentRect
        val valueAnimator = ValueAnimator.ofInt(0, offsetY).setDuration(400L)
        valueAnimator.addUpdateListener(object : AnimatorUpdateListener {
            private var lastX = 0
            private var lastY = 0

            override fun onAnimationUpdate(animation: ValueAnimator) {
                val currY = (animation.animatedValue as Int)
                val currX = (currY * slope).toInt()
                val deltaY = currY - this.lastY
                val deltaX = currX - this.lastX
                this.lastX = currX
                this.lastY = currY
                ViewCompat.offsetTopAndBottom(contentView, deltaY)
                ViewCompat.offsetLeftAndRight(contentView, deltaX)
                val distance = originalRect.height()
                val scrollDistance =
                    max(((contentView.top + deltaY) - originalRect.top).toDouble(), 0.0)
                        .toFloat()
                val alpha =
                    ((1.0f - min(
                        1.0,
                        (scrollDistance / distance).toDouble()
                    )) * dialog.defaultDimAmount).toFloat()
                dialog.window!!.setDimAmount(alpha)
            }
        })
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                dialog.dismiss()
            }
        })
        valueAnimator.start()
    }

    private fun smoothBack(dialog: YOYODialog, offsetY: Int, slope: Float) {
        val contentView = dialog.contentView
        val valueAnimator = ValueAnimator.ofInt(0, offsetY).setDuration(400L)
        valueAnimator.addUpdateListener(object : AnimatorUpdateListener {
            private var lastX = 0
            private var lastY = 0

            override fun onAnimationUpdate(animation: ValueAnimator) {
                val currY = (animation.animatedValue as Int)
                val currX = (currY * slope).toInt()
                val deltaY = currY - this.lastY
                val deltaX = currX - this.lastX
                this.lastX = currX
                this.lastY = currY
                ViewCompat.offsetTopAndBottom(contentView, deltaY)
                ViewCompat.offsetLeftAndRight(contentView, deltaX)
            }
        })
        valueAnimator.start()
    }
}
