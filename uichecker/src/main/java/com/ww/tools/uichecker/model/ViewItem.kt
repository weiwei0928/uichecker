package com.ww.tools.uichecker.model

import android.graphics.Rect

data class ViewItem(
    var id: Int = 0,
    var rootViewId: Int = 0,
    val location: Rect = Rect(),
    val globalLocation: Rect = Rect(),
    var paddingLeft: Int = 0,
    var paddingRight: Int = 0,
    var paddingTop: Int = 0,
    var paddingBottom: Int = 0,
    var leftMargin: Int = 0,
    var rightMargin: Int = 0,
    var topMargin: Int = 0,
    var bottomMargin: Int = 0,
    var visibility: Int = 0,
    var backgroundColor: Int? = null,
    var textColor: Int = 0,
    var textSize: Float = 0f,
    var parentViewItem: ViewItem? = null,
    var leftViewItem: ViewItem? = null,
    var rightViewItem: ViewItem? = null,
    var topViewItem: ViewItem? = null,
    var bottomViewItem: ViewItem? = null,
    var hasChildView: Boolean = false
)