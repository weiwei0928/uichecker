package com.ww.tools.uichecker.drawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.widget.TextView
import com.ww.tools.uichecker.drawer.base.IDrawer
import com.ww.tools.uichecker.utils.DrawUtils.DEBUG_CORNERS_COLOR
import com.ww.tools.uichecker.utils.DrawUtils.drawDistance

class TextViewDrawer(context: Context) : IDrawer(context) {

    fun onDraw(canvas: Canvas, view: TextView) {
        val layout = view.layout
        val bounds = Rect()
        val verticalOffset = view.baseline - layout.getLineBaseline(0)
        val horizontalOffset = view.paddingStart
        val lineCount = layout.lineCount
        for (index in 0 until lineCount) {
            layout.getLineBounds(index, bounds)
            val width = (bounds.width() - (layout.getPrimaryHorizontal(0)
                .toInt())) - (layout.getSecondaryHorizontal(0).toInt())
            bounds.top += verticalOffset
            bounds.bottom += verticalOffset
            bounds.left += horizontalOffset + (layout.getPrimaryHorizontal(0).toInt())
            bounds.right = bounds.left + width
            mPaint.color = DEBUG_CORNERS_COLOR
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = 1.0f
            canvas.drawRect(bounds, mPaint)
            val baseLine = layout.getLineBaseline(index) + verticalOffset
            canvas.drawLine(
                bounds.left.toFloat(),
                baseLine.toFloat(),
                bounds.right.toFloat(),
                baseLine.toFloat(),
                mPaint
            )
        }
        mPaint.textSize = 8.0f
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.RED
        mPaint.textAlign = Paint.Align.LEFT
        val textSize = pixelsToScaleDips(view.textSize)
        val textColors = view.textColors
        val id = try {
            " " + context.resources.getResourceEntryName(view.id)
        } catch (e: Exception) {
            ""
        }
        val textColor = toHexString(textColors.defaultColor)
        val text = "text(" + textSize + "sp, " + textColor + id + ")"
        mPaint.textAlign = Paint.Align.RIGHT
        canvas.drawText(
            text,
            0,
            text.length,
            (bounds.right - 2).toFloat(),
            (bounds.bottom - 2).toFloat(),
            mPaint
        )
        val centerY = ((bounds.bottom - verticalOffset) / 2) + verticalOffset
        val centerX = bounds.left + (bounds.width() / 2)
        mPaint.color = Color.GREEN
        drawDistance(context,canvas, mPaint, 0, centerY, bounds.left, centerY)
        drawDistance(context,canvas, mPaint, centerX, 0, centerX, verticalOffset)
        drawDistance(context,canvas, mPaint, bounds.right, centerY, view.measuredWidth, centerY)
        drawDistance(context,canvas, mPaint, centerX, bounds.bottom, centerX, view.measuredHeight)
    }

}
