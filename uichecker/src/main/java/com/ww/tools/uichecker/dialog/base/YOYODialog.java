package com.ww.tools.uichecker.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.ww.tools.uichecker.R;
import com.ww.tools.uichecker.dialog.base.touchprocessor.BottomTouchProcessor;
import com.ww.tools.uichecker.dialog.base.touchprocessor.CenterTouchProcessor;
import com.ww.tools.uichecker.dialog.base.touchprocessor.DefaultTouchProcessor;
import com.ww.tools.uichecker.dialog.base.touchprocessor.LeftTouchProcessor;
import com.ww.tools.uichecker.dialog.base.touchprocessor.RightTouchProcessor;
import com.ww.tools.uichecker.dialog.base.touchprocessor.TopTouchProcessor;
import com.ww.tools.uichecker.dialog.base.touchprocessor.TouchCancelProcessor;
import com.ww.tools.uichecker.dialog.base.touchprocessor.TouchProcessor;

import java.util.ArrayList;
import java.util.List;

public class YOYODialog extends Dialog {
    private final Builder builder;
    private View contentView;
    private final List<DialogInterface.OnDismissListener> onDismissListeners = new ArrayList<>();
    private final List<TouchProcessor> touchProcessors = new ArrayList<>();
    private final float defaultDimAmount = 0.4f;
    private final Rect contentRect = new Rect();
    private final DialogInterface.OnDismissListener onDismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            for (DialogInterface.OnDismissListener onDismissListener : YOYODialog.this.onDismissListeners) {
                onDismissListener.onDismiss(dialog);
            }
        }
    };

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    private YOYODialog(@NonNull Context context, Builder builder) {
        super(context);
        this.builder = builder;
        initView();
    }

    public YOYODialog(@NonNull Context context, int themeResId, Builder builder) {
        super(context);
        this.builder = builder;
        initView();
    }

    public void initView() {
        ContentLayout contentLayout;
        int i;
        ContentLayout.Builder contentLayoutBuilder = new ContentLayout.Builder(getWindow());
        layoutDialogContent(contentLayoutBuilder);
        contentLayoutBuilder.setNavigationBarColor(this.builder.navigationBarColor);
        if (this.builder.layoutResId != 0) {
            contentLayout = contentLayoutBuilder.setContentView(this.builder.layoutResId).build();
            getWindow().setContentView(contentLayout, createLayoutParams());
        } else {
            contentLayout = contentLayoutBuilder.setContentView(this.builder.contentView).build();
            getWindow().setContentView(contentLayout, createLayoutParams());
            addDismissListener(this.builder.contentView);
            this.builder.contentView.setYoyoDialog(this);
        }
        this.contentView = contentLayout.getContentView();
        this.contentView.post(new Runnable() {
            @Override
            public void run() {
                YOYODialog.this.contentView.getHitRect(YOYODialog.this.contentRect);
            }
        });
        setOnDismissListener(this.onDismissListener);
        addDismissListener(this.builder.onDismissListener);
        if (this.builder.dialogStyle == DialogStyle.SPECIFIC) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.x = (this.builder.dialogStyle.gravity & 65280) >> 16;
            layoutParams.y = this.builder.dialogStyle.gravity & 255;
            getWindow().setAttributes(layoutParams);
        } else {
            getWindow().setGravity(this.builder.dialogStyle.gravity);
        }
        Window window = getWindow();
        if (this.builder.windowAnimations <= 0) {
            i = this.builder.dialogStyle.animStyle;
        } else {
            i = this.builder.windowAnimations;
        }
        window.setWindowAnimations(i);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        setCanceledOnTouchOutside(this.builder.outsideTouchable);
        if (this.builder.outsideTouchable) {
            getWindow().setLayout(-1, -2);
        } else {
            int width = (this.builder.dialogStyle == DialogStyle.TOP || this.builder.dialogStyle == DialogStyle.BOTTOM || this.builder.dialogStyle == DialogStyle.CENTER || this.builder.dialogStyle == DialogStyle.FULLSCREEN) ? -1 : -2;
            int height = (this.builder.dialogStyle == DialogStyle.LEFT || this.builder.dialogStyle == DialogStyle.RIGHT || this.builder.dialogStyle == DialogStyle.FULLSCREEN || (this.builder.dialogStyle == DialogStyle.CENTER && this.builder.slidingDismiss)) ? -1 : -2;
            getWindow().setLayout(width, height);
        }
        getWindow().setDimAmount(0.4f);
        if (this.builder.outsideTouchable) {
            getWindow().setDimAmount(0.0f);
            getWindow().setFlags(32, 32);
            getWindow().setFlags(262144, 262144);
        }
        if (this.builder.cancelable) {
            this.touchProcessors.add(new TouchCancelProcessor());
        }
        if (this.builder.slidingDismiss) {
            this.touchProcessors.add(this.builder.dialogStyle.touchProcessor);
        }
        if (this.builder.showWindowDim) {
            return;
        }
        if (this.builder.dialogStyle == DialogStyle.BOTTOM || this.builder.dialogStyle == DialogStyle.TOP || this.builder.dialogStyle == DialogStyle.FULLSCREEN) {
            getWindow().clearFlags(2);
        }
    }

    private void layoutDialogContent(ContentLayout.Builder contentLayoutBuilder) {
        switch (this.builder.dialogStyle) {
            case LEFT:
            case RIGHT:
            case BOTTOM:
            case CENTER:
            case TOP:
                contentLayoutBuilder.setLayoutType(ContentLayout.LayoutType.ABOVE_NAVIGATION_BAR);
                return;
            case FULLSCREEN:
            case SPECIFIC:
                contentLayoutBuilder.setLayoutType(ContentLayout.LayoutType.FULLSCREEN);
                return;
            default:
        }
    }

    public View getContentView() {
        return this.contentView;
    }

    public Rect getContentRect() {
        return this.contentRect;
    }

    public float getDefaultDimAmount() {
        return this.builder.outsideTouchable ? 0.0f : 0.4f;
    }

    private FrameLayout.LayoutParams createLayoutParams() {
        FrameLayout.LayoutParams layoutParams;
        if (this.builder.dialogStyle == DialogStyle.FULLSCREEN) {
            layoutParams = new FrameLayout.LayoutParams(-1, -1);
            layoutParams.gravity = 48;
        } else {
            int width = (this.builder.dialogStyle == DialogStyle.TOP || this.builder.dialogStyle == DialogStyle.BOTTOM || this.builder.dialogStyle == DialogStyle.CENTER) ? -1 : -2;
            int height = (this.builder.dialogStyle == DialogStyle.LEFT || this.builder.dialogStyle == DialogStyle.RIGHT) ? -1 : -2;
            layoutParams = new FrameLayout.LayoutParams(width, height);
            layoutParams.gravity = this.builder.dialogStyle != DialogStyle.SPECIFIC ? this.builder.dialogStyle.gravity : 48;
        }
        return layoutParams;
    }

    public void dismissDelay(int delay) {
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                YOYODialog.this.dismiss();
            }
        }, delay);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onBackPressed() {
        if (this.builder.cancelable) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.builder.outsideTouchable && 4 == event.getAction()) {
            return true;
        }
        for (TouchProcessor processor : this.touchProcessors) {
            processor.doProcess(this, event);
        }
        return false;
    }

    private void addDismissListener(DialogInterface.OnDismissListener listener) {
        if (listener != null && !this.onDismissListeners.contains(listener)) {
            this.onDismissListeners.add(listener);
        }
    }

    public enum DialogStyle {
        BOTTOM(Gravity.BOTTOM, R.style.BottomDialog, new BottomTouchProcessor()),
        CENTER(Gravity.CENTER, R.style.CenterDialog, new CenterTouchProcessor()),
        TOP(Gravity.TOP, R.style.TopDialog, new TopTouchProcessor()),
        LEFT(Gravity.START, R.style.LeftDialog, new LeftTouchProcessor()),
        RIGHT(Gravity.END, R.style.RightDialog, new RightTouchProcessor()),
        FULLSCREEN(Gravity.CENTER, R.style.CenterDialog, new CenterTouchProcessor()),
        SPECIFIC(-1, R.style.CenterDialog, new DefaultTouchProcessor());

        private int gravity;
        private final int animStyle;
        private final TouchProcessor touchProcessor;

        DialogStyle(int gravity, int animStyle, TouchProcessor touchProcessor) {
            this.gravity = gravity;
            this.animStyle = animStyle;
            this.touchProcessor = touchProcessor;
        }

        public DialogStyle setPosition(int x, int y) {
            this.gravity = (x << 16) | y;
            return this;
        }
    }

    public static class Builder {
        private Context context;
        private int windowAnimations;
        private int layoutResId;
        private YOYODialogView contentView;
        private DialogInterface.OnDismissListener onDismissListener;
        private boolean cancelable = true;
        private DialogStyle dialogStyle = DialogStyle.BOTTOM;
        private boolean outsideTouchable = false;
        private boolean slidingDismiss = false;
        private int navigationBarColor = 0;
        private int dividerColor = 0;
        private boolean showWindowDim = true;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setShowWindowDim(boolean showWindowDim) {
            this.showWindowDim = showWindowDim;
            return this;
        }

        public Builder setNavigationBarColor(int navigationBarColor) {
            this.navigationBarColor = navigationBarColor;
            return this;
        }

        public Builder setDividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder setSlidingDismiss(boolean slidingDismiss) {
            this.slidingDismiss = slidingDismiss;
            return this;
        }

        public Builder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setContentView(YOYODialogView view) {
            this.contentView = view;
            return this;
        }

        public Builder setContentView(@LayoutRes int layoutResId) {
            this.layoutResId = layoutResId;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setDialogStyle(DialogStyle dialogStyle) {
            this.dialogStyle = dialogStyle;
            return this;
        }

        public Builder setOutsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        public Builder setWindowAnimations(int windowAnimations) {
            this.windowAnimations = windowAnimations;
            return this;
        }

        public YOYODialog build() {
            return new YOYODialog(this.context, R.style.FullHeightDialog, this);
        }
    }
}
