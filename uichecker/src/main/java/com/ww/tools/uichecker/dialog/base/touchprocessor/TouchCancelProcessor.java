package com.ww.tools.uichecker.dialog.base.touchprocessor;

import android.view.MotionEvent;

import com.ww.tools.uichecker.dialog.base.YOYODialog;

public class TouchCancelProcessor extends TouchProcessor {
    private MotionEvent lastMotionEvent;
    private boolean hasContentTouched = false;
    private float moveDistance = 0.0f;

    @Override
    public void doProcess(YOYODialog dialog, MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.hasContentTouched = dialog.getContentRect().contains((int) event.getX(), (int) event.getY());
                this.lastMotionEvent = MotionEvent.obtain(event);
                this.moveDistance = 0.0f;
                break;
            case 1:
                if (!this.hasContentTouched) {
                    dialog.dismiss();
                    break;
                }
                break;
            case 2:
                this.moveDistance += Math.abs(event.getX() - this.lastMotionEvent.getX()) + Math.abs(event.getY() - this.lastMotionEvent.getY());
                break;
        }
        this.lastMotionEvent = MotionEvent.obtain(event);
    }
}
