package com.ww.tools.uichecker.dialog.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;

public class NavigationBarUtil {
    public static void hideNavigationBar(final Window window) {
        window.getDecorView().setSystemUiVisibility(2);
        window.getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions = 1798 | 4096;
                } else {
                    uiOptions = 1798 | 1;
                }
                window.getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
    }

    public static void focusNotAle(Window window) {
        window.setFlags(8, 8);
    }

    public static void clearFocusNotAle(Window window) {
        window.clearFlags(8);
    }

    public static boolean checkNavigationBarShow(@NonNull Context context, @NonNull Window window) {
        int systemUiVisility = ((Activity) context).getWindow().getDecorView().getSystemUiVisibility();
        if ((systemUiVisility & 514) == 0) {
            return true;
        }
        return false;
    }
}
