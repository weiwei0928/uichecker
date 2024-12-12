package com.ww.tools.uichecker.dialog.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ContentLayout extends LinearLayout {
    private final Builder builder;
    private View statusBar;
    private View navigationBar;
    private View contentView;

    public enum LayoutType {
        FULLSCREEN,
        ABOVE_NAVIGATION_BAR,
        BELOW_STATUS_BAR,
        BETWEEN_STATUS_BAR_AND_NAVIGATION_BAR
    }
    public ContentLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ContentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.builder = new Builder(null); // 这里可以根据需要进行初始化
        initView();
    }

    public ContentLayout(Context context, Builder builder) {
        super(context);
        this.builder = builder;
        initView();
    }

    public View getContentView() {
        return this.contentView;
    }

    private void initView() {
        setOrientation(LinearLayout.VERTICAL);
        this.statusBar = new FrameLayout(getContext());
        this.navigationBar = new FrameLayout(getContext());
        this.contentView = this.builder.contentView != null ? this.builder.contentView : View.inflate(getContext(), this.builder.contentViewLayoutId, null);
        addView(this.statusBar, new LinearLayout.LayoutParams(-1, -2, 0.0f));
        addView(this.contentView, new LinearLayout.LayoutParams(-1, -2, 1.0f));
        addView(this.navigationBar, new LinearLayout.LayoutParams(-1, -2, 0.0f));
        this.statusBar.setVisibility(this.builder.showStatusBar ? View.VISIBLE : View.GONE);
        this.navigationBar.setVisibility(this.builder.showNavigationBar ? View.VISIBLE : View.GONE);
        if (this.builder.showStatusBar) {
            this.statusBar.setVisibility(View.VISIBLE);
            if (this.builder.statusBarColor != null) {
                this.statusBar.setBackgroundColor(this.builder.statusBarColor);
            } else if (this.builder.statusBarResource != null) {
                this.statusBar.setBackgroundResource(this.builder.statusBarResource);
            }
        }
        if (this.builder.showNavigationBar) {
            this.navigationBar.setVisibility(View.VISIBLE);
            if (this.builder.navigationBarColor != null) {
                this.navigationBar.setBackgroundColor(this.builder.navigationBarColor);
            } else if (this.builder.navigationBarResource != null) {
                this.navigationBar.setBackgroundResource(this.builder.navigationBarResource);
            }
        }
        ViewUtils.doOnApplyWindowInsets(this.builder.window.getDecorView(), new ViewUtils.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat insets, ViewUtils.RelativePadding initialPadding) {
                if (ContentLayout.this.builder.showStatusBar) {
                    initialPadding.top += insets.getSystemWindowInsetTop();
                    initialPadding.applyToView(ContentLayout.this.statusBar);
                    initialPadding.top -= insets.getSystemWindowInsetTop();
                    ViewCompat.setPaddingRelative(ContentLayout.this.statusBar, ViewCompat.getPaddingStart(ContentLayout.this.statusBar), insets.getSystemWindowInsetTop(), ViewCompat.getPaddingEnd(ContentLayout.this.statusBar), ContentLayout.this.statusBar.getPaddingBottom());
                }
                if (ContentLayout.this.builder.showNavigationBar) {
                    ViewCompat.setPaddingRelative(ContentLayout.this.navigationBar, ViewCompat.getPaddingStart(ContentLayout.this.navigationBar), ContentLayout.this.navigationBar.getPaddingTop(), ViewCompat.getPaddingEnd(ContentLayout.this.navigationBar), insets.getSystemWindowInsetBottom());
                }
                return insets;
            }
        });
    }

    public static class Builder {
        private final Window window;
        private Integer navigationBarColor;
        private Integer navigationBarResource;
        private Integer statusBarColor;
        private Integer statusBarResource;
        private View contentView;
        private Integer contentViewLayoutId;
        private Boolean showStatusBar = false;
        private Boolean showNavigationBar = false;

        public Builder(Window window) {
            this.window = window;
            setLayoutType(LayoutType.BETWEEN_STATUS_BAR_AND_NAVIGATION_BAR);
        }

        public void setLayoutType(LayoutType layoutType) {
            switch (layoutType) {
                case BETWEEN_STATUS_BAR_AND_NAVIGATION_BAR:
                    this.showStatusBar = true;
                    this.showNavigationBar = true;
                    break;
                case ABOVE_NAVIGATION_BAR:
                    this.showStatusBar = false;
                    this.showNavigationBar = true;
                    break;
                case BELOW_STATUS_BAR:
                    this.showStatusBar = true;
                    this.showNavigationBar = false;
                    break;
                case FULLSCREEN:
                    this.showStatusBar = false;
                    this.showNavigationBar = false;
                    break;
            }
        }

        public void setNavigationBarColor(int navigationBarColor) {
            this.navigationBarColor = navigationBarColor;
        }

        public Builder setNavigationBarResource(int navigationBarResource) {
            this.navigationBarResource = navigationBarResource;
            return this;
        }

        public Builder setStatusBarColor(int statusBarColor) {
            this.statusBarColor = statusBarColor;
            return this;
        }

        public Builder setStatusBarResource(int statusBarResource) {
            this.statusBarResource = statusBarResource;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder setContentView(int contentViewLayoutId) {
            this.contentViewLayoutId = contentViewLayoutId;
            return this;
        }

        public ContentLayout build() {
            return new ContentLayout(this.window.getContext(), this);
        }
    }
}
