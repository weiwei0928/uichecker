package com.ww.tools.uichecker.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object FloatUtils {
    const val REQUEST_FLOAT_CODE = 1001

    const val FLOAT_VIEW_INIT_X = 52
    const val FLOAT_VIEW_INIT_Y = 500
    /**
     * 判断Service是否开启
     *
     */
    fun isServiceRunning(context: Context, ServiceName: String): Boolean {
        if (TextUtils.isEmpty(ServiceName)) {
            return false
        }
        val myManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningService =
            myManager.getRunningServices(1000) as ArrayList<ActivityManager.RunningServiceInfo>
        for (i in runningService.indices) {
            if (runningService[i].service.className == ServiceName) {
                return true
            }
        }
        return false
    }

    /**
     * 判断悬浮窗权限权限
     */
    private fun commonROMPermissionCheck(context: Context?): Boolean {
        var result = true
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                val clazz: Class<*> = Settings::class.java
                val canDrawOverlays =
                    clazz.getDeclaredMethod("canDrawOverlays", Context::class.java)
                result = canDrawOverlays.invoke(null, context) as Boolean
            } catch (e: Exception) {
                Log.e("commonROMPermissionCheck ", Log.getStackTraceString(e))
            }
        }
        return result
    }

    /**
     * 检查悬浮窗权限是否开启
     */
    fun checkWindowPermission(context: Activity, block: () -> Unit) {
        if (commonROMPermissionCheck(context)) {
            Log.e("checkWindowPermission", "已授权悬浮窗")
            block()
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            if (context is AppCompatActivity) {
                MyActivityResultCallback.register(context) { resultCode, intent ->
                    Log.d("checkWindowPermission", "checkWindowPermission: $resultCode")
                    if (resultCode == Activity.RESULT_OK && intent != null) {
                        Toast.makeText(context, "已授权悬浮窗", Toast.LENGTH_SHORT).show()
                        block()
                    } else {
                        Toast.makeText(context, "未授权悬浮窗", Toast.LENGTH_SHORT).show()
                    }
                }

                MyActivityResultCallback.startActivityForResult(intent)
            } else {
                try {
                    Log.e("checkWindowPermission", "未授权悬浮窗,去往设置页")
                    Toast.makeText(context, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show()
                    context.startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }, REQUEST_FLOAT_CODE)
                } catch (e: Exception) {
                    Log.e("checkWindowPermission", "未授权悬浮窗,errMsg:${e.stackTraceToString()}")
                }
            }

        }
    }


    fun isNull(any: Any?): Boolean = any == null

}