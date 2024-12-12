package com.ww.tools.uichecker.dialog.base

import android.content.Context
import android.content.DialogInterface
import android.util.AttributeSet

open class YOYODialogView : CustomFrameLayout, DialogInterface.OnDismissListener {
    private val onDismissListeners: MutableList<DialogInterface.OnDismissListener?> = ArrayList()
    private var yoyoDialog: YOYODialog? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun addOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        if (listener != null && onDismissListeners.contains(listener)) {
            return
        }
        onDismissListeners.add(listener)
    }

    fun setYoyoDialog(dialog: YOYODialog?) {
        this.yoyoDialog = dialog
    }

    fun dismiss() {
        yoyoDialog!!.dismiss()
    }

    fun dismissDelay(delay: Int) {
        postDelayed({ yoyoDialog!!.dismiss() }, delay.toLong())
    }

    fun dismiss(listener: DialogInterface.OnDismissListener?) {
        addOnDismissListener(listener)
        yoyoDialog!!.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        for (listener in this.onDismissListeners) {
            listener!!.onDismiss(dialog)
        }
    }
}
