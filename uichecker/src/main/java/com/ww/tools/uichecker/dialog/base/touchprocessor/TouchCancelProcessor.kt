package com.ww.tools.uichecker.dialog.base.touchprocessor

import android.view.MotionEvent
import com.ww.tools.uichecker.dialog.base.YOYODialog
import kotlin.math.abs

class TouchCancelProcessor : TouchProcessor() {
    private var lastMotionEvent: MotionEvent? = null
    private var hasContentTouched = false
    private var moveDistance = 0.0f

    override fun doProcess(dialog: YOYODialog, event: MotionEvent) {
        when (event.action) {
            0 -> {
                this.hasContentTouched =
                    dialog.contentRect.contains(event.x.toInt(), event.y.toInt())
                this.lastMotionEvent = MotionEvent.obtain(event)
                this.moveDistance = 0.0f
            }

            1 -> if (!this.hasContentTouched) {
                dialog.dismiss()
            }

            2 -> moveDistance += abs(event.x - lastMotionEvent!!.x) + abs(event.y - lastMotionEvent!!.y)
        }
        this.lastMotionEvent = MotionEvent.obtain(event)
    }
}
