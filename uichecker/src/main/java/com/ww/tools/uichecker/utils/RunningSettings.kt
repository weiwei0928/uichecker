package com.ww.tools.uichecker.utils

import android.app.Application
import android.content.SharedPreferences

object RunningSettings {
    private const val NAME: String = "RunningSettings"
    private var sharedPreferences: SharedPreferences? = null
    private const val KEY_SHOW_LAYOUT_BORDER = "show_layout_border"
    fun init(application: Application) {
        sharedPreferences = application.getSharedPreferences(NAME, 0)
    }

    fun set(key: String, value: String) {
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun get(key: String): String {
        return sharedPreferences?.getString(key, "").toString()
    }

    private fun setBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences!!.getBoolean(key, defaultValue)
    }

    fun setShowLayoutBorder(isShow: Boolean) {
        setBoolean(KEY_SHOW_LAYOUT_BORDER, isShow)
    }

    fun isShowLayoutBorder(): Boolean {
        return getBoolean(KEY_SHOW_LAYOUT_BORDER, false)
    }

}
