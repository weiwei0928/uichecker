package com.ww.tools.uichecker.dialog.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.view.View.OnAttachStateChangeListener;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

@RestrictTo({Scope.LIBRARY_GROUP})
public class ViewUtils {
    private ViewUtils() {
    }

    public static Mode parseTintMode(int value, Mode defaultMode) {
        switch(value) {
            case 3:
                return Mode.SRC_OVER;
            case 4:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            default:
                return defaultMode;
            case 5:
                return Mode.SRC_IN;
            case 9:
                return Mode.SRC_ATOP;
            case 14:
                return Mode.MULTIPLY;
            case 15:
                return Mode.SCREEN;
            case 16:
                return Mode.ADD;
        }
    }

    public static boolean isLayoutRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit = 0) int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(1, (float)dp, r.getDisplayMetrics());
    }

    public static void requestFocusAndShowKeyboard(@NonNull final View view) {
        view.requestFocus();
        view.post(new Runnable() {
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager)view.getContext().getSystemService("input_method");
                inputMethodManager.showSoftInput(view, 1);
            }
        });
    }

    public static void doOnApplyWindowInsets(@NonNull View view, final ViewUtils.OnApplyWindowInsetsListener listener) {
        final ViewUtils.RelativePadding initialPadding = new ViewUtils.RelativePadding(ViewCompat.getPaddingStart(view), view.getPaddingTop(), ViewCompat.getPaddingEnd(view), view.getPaddingBottom());
        ViewCompat.setOnApplyWindowInsetsListener(view, new androidx.core.view.OnApplyWindowInsetsListener() {
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets) {
                return listener != null ? listener.onApplyWindowInsets(view, insets, new ViewUtils.RelativePadding(initialPadding)) : insets;
            }
        });
        requestApplyInsetsWhenAttached(view);
    }

    public static void requestApplyInsetsWhenAttached(@NonNull View view) {
        if (ViewCompat.isAttachedToWindow(view)) {
            ViewCompat.requestApplyInsets(view);
        } else {
            view.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
                public void onViewAttachedToWindow(@NonNull View v) {
                    v.removeOnAttachStateChangeListener(this);
                    ViewCompat.requestApplyInsets(v);
                }

                public void onViewDetachedFromWindow(View v) {
                }
            });
        }

    }

    public static float getParentAbsoluteElevation(@NonNull View view) {
        float absoluteElevation = 0.0F;

        for(ViewParent viewParent = view.getParent(); viewParent instanceof View; viewParent = viewParent.getParent()) {
            absoluteElevation += ViewCompat.getElevation((View)viewParent);
        }

        return absoluteElevation;
    }

    public static boolean isTouchPointInView(View view, float x, float y) {
        if (view == null) {
            return false;
        } else {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int left = location[0];
            int top = location[1];
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            return y >= (float)top && y <= (float)bottom && x >= (float)left && x <= (float)right;
        }
    }

    public static class RelativePadding {
        public int start;
        public int top;
        public int end;
        public int bottom;

        public RelativePadding(int start, int top, int end, int bottom) {
            this.start = start;
            this.top = top;
            this.end = end;
            this.bottom = bottom;
        }

        public RelativePadding(@NonNull ViewUtils.RelativePadding other) {
            this.start = other.start;
            this.top = other.top;
            this.end = other.end;
            this.bottom = other.bottom;
        }

        public void applyToView(View view) {
            ViewCompat.setPaddingRelative(view, this.start, this.top, this.end, this.bottom);
        }
    }

    public interface OnApplyWindowInsetsListener {
        WindowInsetsCompat onApplyWindowInsets(View var1, WindowInsetsCompat var2, ViewUtils.RelativePadding var3);
    }
}

