package com.example.myapplication.floatview

interface FloatViewEvent {
    fun updateLayout(moveX: Int, moveY: Int)
    fun onBounceBack()
    fun onClick()
}