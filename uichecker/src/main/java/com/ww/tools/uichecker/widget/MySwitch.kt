package com.ww.tools.uichecker.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Switch

/**
 * @Author weiwei
 * @Date 2024/12/16 20:31
 */


@SuppressLint("UseSwitchCompatOrMaterialCode")
class MySwitch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Switch(context, attrs, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 只处理 ACTION_UP 事件（即点击释放），忽略其他触摸事件
        if (event.action == MotionEvent.ACTION_UP) {
            toggle() // 切换开关状态
            return true // 消费此事件
        }
        return false // 不消费其他事件，阻止它们影响开关
    }

    override fun performClick(): Boolean {
        toggle() // 确保点击事件也会切换开关状态
        return super.performClick()
    }
}