package com.ww.tools.uichecker.drawable

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import com.ww.tools.uichecker.drawer.InfoDrawer
import com.ww.tools.uichecker.drawer.OutLineDrawer
import com.ww.tools.uichecker.drawer.RelativePositionDrawer
import com.ww.tools.uichecker.drawer.base.ViewGroupDrawer
import com.ww.tools.uichecker.model.ViewItem
import com.ww.tools.uichecker.model.ViewItemFactory


class ViewGroupDrawable(private val rootView: ViewGroup) : Drawable() {
    private val viewItems: ArrayList<ViewItem> = ArrayList<ViewItem>()
    private val drawers: ArrayList<ViewGroupDrawer> = ArrayList<ViewGroupDrawer>()

    init {
        val arrayList: ArrayList<ViewGroupDrawer> = this.drawers
        val context = this.rootView.context
        arrayList.add(OutLineDrawer(context))
        val arrayList2: ArrayList<ViewGroupDrawer> = this.drawers
        arrayList2.add(RelativePositionDrawer(context))
        val arrayList3: ArrayList<ViewGroupDrawer> = this.drawers
        arrayList3.add(InfoDrawer(context))
    }

    override fun draw(canvas: Canvas) {
        viewItems.clear()
        viewItems.addAll(generateChildViewItems())
        val it: Iterator<ViewGroupDrawer> = drawers.iterator()
        while (it.hasNext()) {
            val drawer: ViewGroupDrawer = it.next()
            drawer.onDraw(canvas, this.viewItems)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    private fun generateChildViewItems(): ArrayList<ViewItem> {
        val childViewItems: ArrayList<ViewItem> = ArrayList()
        val parentViewItem: ViewItem =
            ViewItemFactory.generateParentViewItem(this.rootView)
        val childCount = rootView.childCount
        for (i in 0 until childCount) {
            val childView = rootView.getChildAt(i)
            if (childView.visibility == 0) {
                val childViewItem: ViewItem = ViewItemFactory.generateViewItem(childView)
                childViewItem.parentViewItem = parentViewItem
                childViewItems.add(childViewItem)
            }
        }
        if (childViewItems.size > 1) {
            val size = childViewItems.size - 1
            for (i2 in 0 until size) {
                val viewItem: ViewItem = childViewItems[i2]
                val viewItem2: ViewItem = viewItem
                val size2 = childViewItems.size
                for (j in i2 + 1 until size2) {
                    val viewItem3: ViewItem = childViewItems[j]
                    val refViewItem: ViewItem = viewItem3
                    calcRelativePosition(viewItem2, refViewItem)
                }
            }
        }
        return childViewItems
    }

    private fun calcRelativePosition(currViewItem: ViewItem, refViewItem: ViewItem) {

        /
        val rect1 = Rect(
            currViewItem.globalLocation.left,
            currViewItem.globalLocation.top,
            currViewItem.globalLocation.right,
            currViewItem.globalLocation.bottom
        )
        val rect2 = Rect(
            refViewItem.globalLocation.left,
            refViewItem.globalLocation.top,
            refViewItem.globalLocation.right,
            refViewItem.globalLocation.bottom
        )

        // Calculate relative positions.
        val relativeLeft = rect2.left - rect1.left
        val relativeTop = rect2.top - rect1.top
        val relativeRight = rect2.right - rect1.right
        val relativeBottom = rect2.bottom - rect1.bottom

        println("Child 2 is at ($relativeLeft, $relativeTop) relative to Child 1.")
        println("Child 2's bottom-right corner is at ($relativeRight, $relativeBottom) relative to Child 1's bottom-right.")



    }
}
