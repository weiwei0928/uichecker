package com.ww.tools.uichecker.floatview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import com.example.myapplication.floatview.FloatViewEvent
import com.ww.tools.uichecker.R

class FloatView(
    var context: Context,
    var windowManagerHelper: WindowManagerHelper
) : FloatViewEvent {

    private var floatRootView: ViewGroup? = null // 悬浮窗View
    private var isShowOriginalView = true
    private lateinit var layoutOriginal: LinearLayout
    private lateinit var layoutAdsorbed: LinearLayout
    private var layoutOriginalWidth = 0
    private var layoutAdsorbedWidth = 0
    private var layoutWidthOffset = 0
    private var screenWidth = 0

    companion object {
        private const val TAG = "FloatView.Log"
    }

    init {
//        floatRootView =
//            LayoutInflater.from(context).inflate(R.layout.light_float_item, null) as ViewGroup?
//        floatRootView?.apply {
//            layoutOriginal = findViewById(R.id.float_content)
//            layoutAdsorbed = findViewById(R.id.float_small)
//            layoutOriginalWidth = layoutOriginal.layoutParams.width
//            layoutAdsorbedWidth = layoutAdsorbed.layoutParams.width
//            layoutWidthOffset = Math.abs(layoutOriginalWidth - layoutAdsorbedWidth)
//            visibility = View.GONE
//            // 动态设置吸附view的高度
//            viewTreeObserver.addOnPreDrawListener {
//                layoutAdsorbed.layoutParams.height = height
//                true
//            }
//            setOnTouchListener(FloatViewTouchListener(this@FloatView))
//        }
        val displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics.widthPixels
//        Log.e(
//            "floatView",
//            "screenWidth:${screenWidth},layoutOne:${layoutOriginalWidth},layoutTwo:${layoutAdsorbedWidth}"
//        )
//        Log.e("floatView", "can move max width:${screenWidth - layoutOriginalWidth}")
    }

    fun getView(): View {
        return floatRootView!!
    }

    fun hidden() {
        floatRootView?.visibility = View.GONE
    }

    fun show() {
        floatRootView?.visibility = View.VISIBLE
    }

    private fun showOriginalView1() {
        if (!isShowOriginalView) {
            layoutOriginal.visibility = View.VISIBLE
            layoutAdsorbed.visibility = View.GONE
            isShowOriginalView = true
        }
    }

    private fun showAdsorbedView1(useInvisible: Boolean = false) {
        if (isShowOriginalView) {
            if (windowManagerHelper.layoutParams!!.x < screenWidth / 2) {
                layoutAdsorbed.rotation = 0f
            } else {
                layoutAdsorbed.rotation = 180f
            }
            layoutAdsorbed.visibility = View.VISIBLE
            layoutOriginal.visibility = if (useInvisible) View.INVISIBLE else View.GONE
            isShowOriginalView = false
        }
    }


    override fun updateLayout(moveX: Int, moveY: Int) {
        val newX = windowManagerHelper.layoutParams!!.x + moveX
        val newY = windowManagerHelper.layoutParams!!.y + moveY
//        Log.e("floatView", "newX:${newX},this time moveX:${moveX},moveY:${newY}")
        if (newX < 0) {
            handleLeftEdge(moveX, layoutOriginal, layoutWidthOffset)
        } else if (newX > screenWidth - layoutOriginalWidth) {
            handleRightEdge(moveX, floatRootView!!, layoutWidthOffset)
        } else {
            showOriginalView1()
            if (adjustTranslationX(floatRootView!!, moveX)
                || adjustTranslationX(layoutOriginal, moveX)
            ) {
                return
            }
        }
        windowManagerHelper.layoutParams?.apply {
            x = newX
            y = newY
        }
        windowManagerHelper.updateLayout(floatRootView!!)
    }

    /**
     * 左边贴边
     */
    private fun handleLeftEdge(moveX: Int, view: View, offset: Int) {
        if (Math.abs(view.translationX) < offset) {
            view.translationX += moveX
            if (Math.abs(view.translationX) >= offset) {
                view.translationX = -offset.toFloat()
                showAdsorbedView1()
            }
        } else {
            showAdsorbedView1()
        }
    }

    /**
     * 右边贴边
     */
    private fun handleRightEdge(moveX: Int, view: View, offset: Int) {
        if (moveX > 0 && Math.abs(view.translationX) < offset) {
            view.translationX += moveX
            if (Math.abs(view.translationX) >= offset) {
                view.translationX = offset.toFloat()
                showAdsorbedView1(true)
            }
        } else if (moveX <= 0) {
            showOriginalView1()
            adjustTranslationX(view, moveX)
        } else {
            showAdsorbedView1(true)
        }
    }


    /**
     * 修正偏移量
     */
    private fun adjustTranslationX(view: View, moveX: Int): Boolean {
        view.let {
            if (it.translationX < 0) {
                it.translationX += moveX
                if (it.translationX > 0) {
                    it.translationX = 0f
                }
                return true
            } else if (it.translationX > 0) {
                it.translationX += moveX
                if (it.translationX < 0) {
                    it.translationX = 0f
                }
                return true
            }
        }
        return false
    }


    override fun onBounceBack() {
        // 重置windowManagerHelper X值
        windowManagerHelper.layoutParams!!.apply {
            x = if (x < 0) 0 else x
        }

        if (inAdsorbed()) {
            bounceBackToAdsorbed()
        } else {
            bounceBackToOriginal()
        }
    }

    override fun onClick() {
//        Log.e(TAG,">>>>>>>>>点击了>>>>>>>>>>")
        if (isShowOriginalView) {
            // 唤醒activity
//            context.startActivity(Intent(context, MainActivity2::class.java))
            val launchIntentForPackage =
                context.packageManager.getLaunchIntentForPackage(context.packageName)
            context.startActivity(launchIntentForPackage)

        } else {
            resetTranslateX()
            showOriginalView1()
            bounceBackToOriginal()
        }

    }

    private fun bounceBackToAdsorbed() {
        if (!isShowOriginalView) {
            return
        }
        val startX = windowManagerHelper.layoutParams!!.x
        val endX = if (startX < screenWidth / 2) {
            0
        } else {
            screenWidth - layoutAdsorbedWidth
        }
//        Log.e("floatView", "展示吸附状态1 >>>> start:${startX},end:${endX}")

        val valueAnimator = ValueAnimator.ofInt(startX, endX).apply {
            addUpdateListener {
                windowManagerHelper.layoutParams?.apply {
                    x = it.animatedValue as Int
                }
                windowManagerHelper.updateLayout(floatRootView!!)
            }
            addListener(object : AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    if (startX < screenWidth / 2) {
                        showAdsorbedView1()
                        layoutOriginal.translationX = (-layoutWidthOffset).toFloat()
                    } else {
                        showAdsorbedView1(true)
                        floatRootView!!.translationX = layoutWidthOffset.toFloat()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }

            })
            duration = 100
            interpolator = LinearInterpolator()
            start()
        }
    }

    private fun bounceBackToOriginal() {
        val startX = windowManagerHelper.layoutParams?.x ?: 0
        val endX = if (startX < screenWidth / 2) {
            FloatUtils.FLOAT_VIEW_INIT_X
        } else {
            screenWidth - FloatUtils.FLOAT_VIEW_INIT_X - layoutOriginalWidth
        }
        val valueAnimator = ValueAnimator.ofInt(startX, endX).apply {
            addUpdateListener {
                windowManagerHelper.layoutParams?.apply {
                    x = it.animatedValue as Int
                }
                windowManagerHelper.updateLayout(floatRootView!!)
            }
            duration = 200
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    /**
     * 贴边动画
     */
    private var _lastAnimatedValue = 0
    private fun doEdgeAnimator(startX: Int) {
        _lastAnimatedValue = 0
        val endX = if (startX < screenWidth / 2) {
            layoutWidthOffset - Math.abs(layoutOriginal.translationX)
        } else {
            layoutWidthOffset - Math.abs(floatRootView!!.translationX)
        }
//        Log.e(TAG, "doEdgeAnimator start=0,end=${endX}")
        ValueAnimator.ofInt(0, endX.toInt()).apply {
            addUpdateListener {
                val moveX = (it.animatedValue as Int) - _lastAnimatedValue
                _lastAnimatedValue = (it.animatedValue as Int)
                if (moveX > 0) {
                    if (startX < screenWidth / 2) {
                        handleLeftEdge(-moveX, layoutOriginal, layoutWidthOffset)
                    } else {
                        handleRightEdge(moveX, floatRootView!!, layoutWidthOffset)
                    }
                }
            }
            duration = 50
            interpolator = LinearInterpolator()
            start()
        }
    }

    /**
     * 是否处于贴边阈值
     */
    private fun inAdsorbed(): Boolean {
        val x = windowManagerHelper.layoutParams!!.x
        val result = if (x < screenWidth / 2) {
            x < FloatUtils.FLOAT_VIEW_INIT_X - 10
        } else {
            x > screenWidth - layoutOriginalWidth - FloatUtils.FLOAT_VIEW_INIT_X + 10
        }
        return result
    }

    private fun resetTranslateX() {
        if (layoutOriginal.translationX != 0f) {
            layoutOriginal.translationX = 0f
        }
        if (floatRootView!!.translationX != 0f) {
            floatRootView!!.translationX = 0f
        }
    }

}