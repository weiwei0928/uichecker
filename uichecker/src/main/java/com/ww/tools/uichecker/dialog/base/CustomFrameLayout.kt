package com.ww.tools.uichecker.dialog.base

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

open class CustomFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ViewModelStoreOwner, LifecycleOwner {
    private var lifecycleRegistry: LifecycleRegistry? = null

    init {
        initView()
    }

    private fun initView() {
        this.lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    override fun onAttachedToWindow() {
        lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        lifecycleRegistry!!.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        super.onDetachedFromWindow()
    }

    override val viewModelStore: ViewModelStore
        get() = ViewModelStore()

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry!!
}
