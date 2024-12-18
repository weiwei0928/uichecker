package com.ww.tools.uichecker.utils

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

/**
 * @Author weiwei
 * @Date 2024/12/18 01:18
 */
object MyActivityResultCallback {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    fun register(activity: AppCompatActivity, callback: (resultCode: Int, data: Intent?) -> Unit) {
        activityResultLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                callback(result.resultCode, result.data)
            }
    }

    fun startActivityForResult(intent: Intent?) {
        activityResultLauncher.launch(intent)
    }
}