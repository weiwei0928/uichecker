package com.ww.tools.uichecker.dialog.base.touchprocessor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;

import com.ww.tools.uichecker.dialog.base.YOYODialog;

public class TopTouchProcessor extends TouchProcessor {
    private MotionEvent lastMotionEvent;
    private boolean hasContentTouched = false;
    private long downTime;

    @Override
    public void doProcess(YOYODialog dialog, MotionEvent event) {
        Rect originalRect = dialog.getContentRect();
        View contentView = dialog.getContentView();
        switch (event.getAction()) {
            case 0:
                this.hasContentTouched = originalRect.contains((int) event.getX(), (int) event.getY());
                this.downTime = System.currentTimeMillis();
                break;
            case 1:
                if (this.hasContentTouched) {
                    Rect currRect = new Rect();
                    contentView.getHitRect(currRect);
                    int deltaX = originalRect.left - currRect.left;
                    int deltaY = originalRect.top - currRect.top;
                    float slope = (deltaX * 1.0f) / deltaY;
                    long deltaTime = System.currentTimeMillis() - this.downTime;
                    float slideSpeed = (deltaY * 1.0f) / ((float) deltaTime);
                    if (deltaY >= currRect.height() / 2 || slideSpeed > 1.0f) {
                        smoothDismiss(dialog, -currRect.bottom, slope);
                        break;
                    } else {
                        smoothBack(dialog, deltaY, slope);
                        break;
                    }
                }
                break;
            case 2:
                if (this.hasContentTouched) {
                    float deltaY2 = event.getY() - this.lastMotionEvent.getY();
                    boolean isSliding = false;
                    if (deltaY2 < 0.0f) {
                        ViewCompat.offsetTopAndBottom(contentView, (int) deltaY2);
                        isSliding = true;
                    } else if (contentView.getBottom() + deltaY2 <= originalRect.bottom) {
                        ViewCompat.offsetTopAndBottom(contentView, (int) deltaY2);
                        isSliding = true;
                    }
                    if (isSliding) {
                        int distance = originalRect.height();
                        float scrollDistance = Math.max(originalRect.bottom - (contentView.getBottom() + deltaY2), 0.0f);
                        float alpha = (1.0f - Math.min(1.0f, scrollDistance / distance)) * dialog.getDefaultDimAmount();
                        dialog.getWindow().setDimAmount(alpha);
                        break;
                    }
                }
                break;
        }
        this.lastMotionEvent = MotionEvent.obtain(event);
    }

    private void smoothDismiss(final YOYODialog dialog, int offsetY, final float slope) {
        final View contentView = dialog.getContentView();
        final Rect originalRect = dialog.getContentRect();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, offsetY).setDuration(400L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private int lastX = 0;
            private int lastY = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currY = ((Integer) animation.getAnimatedValue()).intValue();
                int currX = (int) (currY * slope);
                int deltaY = currY - this.lastY;
                int deltaX = currX - this.lastX;
                this.lastX = currX;
                this.lastY = currY;
                ViewCompat.offsetTopAndBottom(contentView, deltaY);
                ViewCompat.offsetLeftAndRight(contentView, deltaX);
                int distance = originalRect.height();
                float scrollDistance = Math.max(originalRect.bottom - (contentView.getBottom() + deltaY), 0);
                float alpha = (1.0f - Math.min(1.0f, scrollDistance / distance)) * dialog.getDefaultDimAmount();
                dialog.getWindow().setDimAmount(alpha);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dialog.dismiss();
            }
        });
        valueAnimator.start();
    }

    private void smoothBack(YOYODialog dialog, int offsetY, final float slope) {
        final View contentView = dialog.getContentView();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, offsetY).setDuration(400L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private int lastX = 0;
            private int lastY = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currY = ((Integer) animation.getAnimatedValue()).intValue();
                int currX = (int) (currY * slope);
                int deltaY = currY - this.lastY;
                int deltaX = currX - this.lastX;
                this.lastX = currX;
                this.lastY = currY;
                ViewCompat.offsetTopAndBottom(contentView, deltaY);
                ViewCompat.offsetLeftAndRight(contentView, deltaX);
            }
        });
        valueAnimator.start();
    }
}
