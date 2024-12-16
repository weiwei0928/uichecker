package com.ww.tools.uichecker.floatview

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import com.example.myapplication.floatview.FloatViewEvent

class FloatViewTouchListener(
    var floatViewEvent: FloatViewEvent
) :
    View.OnTouchListener {
    private var x = 0
    private var y = 0
    private var downX = 0
    private var downY = 0
    private var isDownEvent = false
    private var CLICK_ACTION_THRESHHOLD = 5

    private var mHandler = Handler()
    private var runnable = Runnable {
        floatViewEvent.onBounceBack()
    }


    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
//        Log.e("floatEvent"," onTouch eventName=${getActionName(motionEvent.action)}")
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                x = motionEvent.rawX.toInt()
                y = motionEvent.rawY.toInt()
                downX = motionEvent.rawX.toInt()
                downY = motionEvent.rawX.toInt()
                isDownEvent = true
            }

            MotionEvent.ACTION_MOVE -> {
                mHandler.removeCallbacks(runnable)
                val nowX = motionEvent.rawX.toInt()
                val nowY = motionEvent.rawY.toInt()
                val movedX = nowX - x
                val movedY = nowY - y
                x = nowX
                y = nowY

                // 滑动阈值
                if (isDownEvent &&
                    Math.abs(nowX - downX) > CLICK_ACTION_THRESHHOLD
                    && Math.abs(nowY - downY) > CLICK_ACTION_THRESHHOLD
                ) {
                    isDownEvent = false
                }

                if(!isDownEvent){
                    floatViewEvent.updateLayout(movedX, movedY)
                }
            }

            MotionEvent.ACTION_UP -> {
//                Log.e("floatEvent","1=${motionEvent.eventTime - motionEvent.downTime < 150} " +
//                        "2=${motionEvent.pointerCount ==1} " +
//                        "3=${isDownEvent} " )
                if (motionEvent.eventTime - motionEvent.downTime < 150
                    && motionEvent.pointerCount == 1
                    && isDownEvent
                ) {
                    floatViewEvent.onClick()
                } else {
//                    mHandler.postDelayed(runnable, 2500)
                    floatViewEvent.onBounceBack()
                }
                resetDownEvent()
            }

            MotionEvent.ACTION_CANCEL -> {
                floatViewEvent.onBounceBack()
                resetDownEvent()
            }

            else -> {
                resetDownEvent()
            }
        }
        return false
    }

    private fun resetDownEvent(){
        isDownEvent = false
        downX = 0
        downY = 0
    }

    private fun getActionName(action:Int): String {
        return when(action){
            MotionEvent.ACTION_DOWN-> "ACTION_DOWN"
            MotionEvent.ACTION_MOVE-> "ACTION_MOVE"
            MotionEvent.ACTION_UP-> "ACTION_UP"
            MotionEvent.ACTION_CANCEL-> "ACTION_CANCEL"
            else -> "UNKNOWN"
        }
    }
}