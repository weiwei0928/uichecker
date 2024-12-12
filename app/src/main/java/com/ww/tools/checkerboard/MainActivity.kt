package com.ww.tools.checkerboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.ww.tools.uichecker.UIChecker
import com.ww.tools.checkerboard.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()

        setContentView(R.layout.activity_main)
        UIChecker.manualInstall(this.application)
    }
}
