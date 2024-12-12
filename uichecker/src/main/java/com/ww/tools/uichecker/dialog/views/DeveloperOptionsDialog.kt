package com.ww.tools.uichecker.dialog.views

import android.app.Activity
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import com.ww.tools.uichecker.R
import com.ww.tools.uichecker.dialog.base.YOYODialogView
import com.ww.tools.uichecker.utils.RunningSettings
import com.ww.tools.uichecker.widget.BorderViewFrameLayout

class DeveloperOptionsDialog(activity: Activity) : YOYODialogView(activity) {
    private val switchButton: Switch

    init {
        inflate(context, R.layout.dialog_dev_options, this)
        this.switchButton = findViewById<Switch>(R.id.show_layout_border_switch)
        findViewById<View>(R.id.show_layout_border_item).setOnClickListener {
            switchButton.isChecked = !RunningSettings.isShowLayoutBorder()
        }
        switchButton.isChecked = RunningSettings.isShowLayoutBorder()
        switchButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->

            RunningSettings.setShowLayoutBorder(isChecked)
            if (isChecked) {
                if (!BorderViewFrameLayout.isInstalled(activity)) {
                    BorderViewFrameLayout.install(activity)
                    return@OnCheckedChangeListener
                }
                return@OnCheckedChangeListener
            }
            BorderViewFrameLayout.uninstalled(activity)
        })
    }
}
