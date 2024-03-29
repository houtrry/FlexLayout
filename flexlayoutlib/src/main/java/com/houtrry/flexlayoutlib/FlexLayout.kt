package com.houtrry.flexlayoutlib

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup

class FlexLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attributeSet, defStyleAttr) {

    companion object {
        private val TAG = FlexLayout::class.java.simpleName
    }

    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        Log.d(TAG, "onMeasure start")
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        Log.d(TAG, "widthMode: $widthMode, widthSize: $widthSize")
        Log.d(TAG, "heightMode: $heightMode, heightSize: $heightSize")

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var horizontalWidth: Int = 0
        var verticalHeight: Int = 0

        var maxLineWidth: Int = 0
        var maxLineHeight: Int = 0

        var resultWidth: Int = 0
        var resultHeight: Int = 0


        Log.d(TAG, "childCount: $childCount")

        //子控件一行能显示完
        var isSingleLine = true
        for (index in 0 until childCount) {
            val child = getChildAt(index)
            var childParams = child.layoutParams as MarginLayoutParams

            var childWidth = child.measuredWidth + childParams.leftMargin + childParams.rightMargin
            var childHeight = child.measuredHeight + childParams.topMargin + childParams.bottomMargin



            Log.d(TAG, "index: $index, childWidth: $childWidth, childHeight: $childHeight")
            Log.d(
                TAG,
                "index: $index, horizontalWidth: $horizontalWidth, childWidth: $childWidth, widthSize: $widthSize, ${horizontalWidth + childWidth} -> $widthSize"
            )

            if (horizontalWidth + childWidth > widthSize) {
                isSingleLine = false
                Log.d(TAG, "index: $index, 需要换行")
                //需要换行
                maxLineHeight = childHeight
                resultHeight += maxLineHeight

                maxLineWidth = if (maxLineWidth > horizontalWidth) maxLineWidth else horizontalWidth
                horizontalWidth = childWidth
                Log.d(
                    TAG,
                    "index: $index, resultHeight: $resultHeight, maxLineHeight: $maxLineHeight, maxLineWidth: $maxLineWidth, horizontalWidth: $horizontalWidth"
                )
            } else {
                Log.d(TAG, "index: $index, 不换行")
                //不换行
                horizontalWidth += childWidth
                maxLineHeight = if (maxLineHeight > childHeight) maxLineHeight else childHeight
                Log.d(TAG, "index: $index, horizontalWidth: $horizontalWidth, maxLineHeight: $maxLineHeight")
            }
        }

        if (isSingleLine) {
            maxLineWidth = horizontalWidth
            resultHeight = maxLineHeight
        } else {
            resultHeight += maxLineHeight
        }

        resultWidth = if (widthMode == MeasureSpec.AT_MOST) maxLineWidth else widthSize
        resultHeight = if (heightMode == MeasureSpec.AT_MOST) resultHeight else heightSize

        Log.d(TAG, "maxLineWidth: $maxLineWidth, widthSize: $widthSize")
        Log.d(TAG, "resultHeight: $resultHeight, heightSize: $heightSize")
        Log.d(TAG, "resultWidth: $resultWidth, resultHeight: $resultHeight")
        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        var left:Int = paddingLeft
        var top:Int = paddingTop
        var right:Int = 0
        var bottom:Int = 0

        for (index in 0 until childCount) {
            val childView = getChildAt(index)
            if (childView.visibility == View.GONE) {
                continue
            }

            val layoutParams = childView.layoutParams as MarginLayoutParams

            val childWidth = childView.measuredWidth
            val childHeight = childView.measuredHeight


            left += layoutParams.leftMargin
            right = left + childWidth

            if (right > measuredWidth) {
                Log.d(TAG, "onLayout, 换行")
                left = paddingLeft + layoutParams.leftMargin
                right = left + childWidth

                top = bottom + layoutParams.topMargin
                bottom = top + childHeight
            } else {
                Log.d(TAG, "onLayout, 不换行")
                top += layoutParams.topMargin
                bottom = top + childHeight
            }




            Log.d(TAG, "onLayout, left: $left, right: $right, top: $top, bottom: $bottom, measuredWidth: $measuredWidth, childHeight: $childHeight")

            childView.layout(left, top, right, bottom)

            left = right + layoutParams.rightMargin

        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(0, 0)
    }
}