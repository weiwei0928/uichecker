package com.ww.tools.uichecker.dialog.base.touchprocessor;

import android.view.MotionEvent;

import com.ww.tools.uichecker.dialog.base.YOYODialog;

public abstract class TouchProcessor {
    public abstract void doProcess(YOYODialog yOYODialog, MotionEvent motionEvent);
}
