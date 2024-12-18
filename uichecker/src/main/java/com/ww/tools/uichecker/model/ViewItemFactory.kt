package com.ww.tools.uichecker.model

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.TextView

object ViewItemFactory {
    fun generateViewItem(view: View): ViewItem {
        val viewItem = ViewItem()
        fillViewItem(viewItem, view)
        return viewItem
    }

    fun generateParentViewItem(view: View): ViewItem {
        val viewItem = ViewItem()
        fillParentViewItem(viewItem, view)
        return viewItem
    }

    private fun fillViewItem(viewItem: ViewItem, view: View) {
        viewItem.id = view.id
        viewItem.rootViewId = view.rootView.hashCode()
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        viewItem.globalLocation.left = location[0]
        viewItem.globalLocation.top = location[1]
        viewItem.globalLocation.right = location[0] + view.measuredWidth
        viewItem.globalLocation.bottom = location[1] + view.measuredHeight
        viewItem.hasChildView = (view is ViewGroup) && view.childCount > 0
        view.getHitRect(viewItem.location)
        viewItem.visibility =
            if ((view.measuredWidth <= 0 || view.measuredHeight <= 0)) 8 else view.visibility
        viewItem.paddingLeft = view.paddingLeft
        viewItem.paddingRight = view.paddingRight
        viewItem.paddingTop = view.paddingTop
        viewItem.paddingBottom = view.paddingBottom
        val layoutParams = view.layoutParams
        if (layoutParams is MarginLayoutParams) {
            val marginLayoutParams = layoutParams as MarginLayoutParams
            viewItem.leftMargin = marginLayoutParams.leftMargin
        }

        val layoutParams2 = view.layoutParams
        if (layoutParams2 is MarginLayoutParams) {
            val marginLayoutParams2 = layoutParams2 as MarginLayoutParams
            viewItem.rightMargin = marginLayoutParams2.rightMargin
        }

        val layoutParams3 = view.layoutParams
        if (layoutParams3 is MarginLayoutParams) {
            val marginLayoutParams3 = layoutParams3 as MarginLayoutParams
            viewItem.topMargin = marginLayoutParams3.topMargin
        }

        val layoutParams4 = view.layoutParams
        if (layoutParams4 is MarginLayoutParams) {
            val marginLayoutParams4 = layoutParams4 as MarginLayoutParams
            viewItem.bottomMargin = marginLayoutParams4.bottomMargin
        }

        val background = view.background
        if (background != null && (background is ColorDrawable)) {
            viewItem.backgroundColor = background.color
        }
        if (view is TextView) {
            val textColors = view.textColors
            viewItem.textColor = textColors.defaultColor
            viewItem.textSize = view.textSize
        }
    }

    private fun fillParentViewItem(viewItem: ViewItem, view: View) {
        viewItem.id = view.id
        viewItem.rootViewId = view.rootView.hashCode()
        viewItem.location.left = 0
        viewItem.location.top = 0
        viewItem.location.right = view.measuredWidth
        viewItem.location.bottom = view.measuredHeight
        viewItem.visibility =
            if ((view.measuredWidth <= 0 || view.measuredHeight <= 0)) 8 else view.visibility
        viewItem.paddingLeft = view.paddingLeft
        viewItem.paddingRight = view.paddingRight
        viewItem.paddingTop = view.paddingTop
        viewItem.paddingBottom = view.paddingBottom
        val layoutParams = view.layoutParams
        if (layoutParams is MarginLayoutParams) {
            val marginLayoutParams = layoutParams as MarginLayoutParams
            viewItem.leftMargin = marginLayoutParams.leftMargin
        }

        val layoutParams2 = view.layoutParams
        if (layoutParams2 is MarginLayoutParams) {
            val marginLayoutParams2 = layoutParams2 as MarginLayoutParams
            viewItem.rightMargin = marginLayoutParams2.rightMargin
        }

        val layoutParams3 = view.layoutParams
        if (layoutParams3 is MarginLayoutParams) {
            val marginLayoutParams3 = layoutParams3 as MarginLayoutParams
            viewItem.topMargin = marginLayoutParams3.topMargin
        }

        val layoutParams4 = view.layoutParams
        if (layoutParams4 is MarginLayoutParams) {
            val marginLayoutParams4 = layoutParams4 as MarginLayoutParams
            viewItem.bottomMargin = marginLayoutParams4.bottomMargin
        }

        viewItem.hasChildView = (view is ViewGroup) && view.childCount > 0
        val background = view.background
        if (background != null && (background is ColorDrawable)) {
            viewItem.backgroundColor = background.color
        }
    }

}
