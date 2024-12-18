package com.ww.tools.uichecker


import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import com.ww.tools.uichecker.floatview.FloatViewHelper
import com.ww.tools.uichecker.utils.SpUtil.init

object UIChecker {

    private const val TAG = "UIChecker-aaaa"

    fun manualInstall(application: Application) {
        init(application)
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//                LauncherView.install(activity)
                Log.d(TAG, "onActivityCreated: $activity")

            }

            override fun onActivityStarted(activity: Activity) {
                FloatViewHelper.init(activity)
            }

            override fun onActivityResumed(activity: Activity) = Unit

            override fun onActivityPaused(activity: Activity) = Unit

            override fun onActivityStopped(activity: Activity) = Unit

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

            override fun onActivityDestroyed(activity: Activity) {
                Log.d(TAG, "onActivityDestroyed: $activity")
//                FloatViewHelper.uninstall()
            }
        })
    }
}
