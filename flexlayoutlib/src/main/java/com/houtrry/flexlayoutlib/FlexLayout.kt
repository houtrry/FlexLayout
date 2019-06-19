package com.houtrry.flexlayoutlib

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class FlexLayout : ViewGroup {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, -1)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    init {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var horizontalWidth: Int = 0
        var verticalHeight: Int = 0

        var maxLineWidth: Int = 0
        var maxLineHeight: Int = 0

        var resultWidth: Int = 0
        var resultHeight: Int = 0

        for (index in 0 until childCount - 1) {
            val child = getChildAt(index)
            var childParams = child.layoutParams as MarginLayoutParams

            var childWidth = child.measuredWidth + childParams.leftMargin + childParams.rightMargin
            var childHeight = child.measuredHeight + childParams.topMargin + childParams.bottomMargin


            if (horizontalWidth + childWidth > widthSize) {
                //需要换行
                resultHeight += maxLineHeight
                maxLineHeight = childHeight

                maxLineWidth = if (maxLineWidth > horizontalWidth) maxLineWidth else horizontalWidth
                horizontalWidth = childWidth
            } else {
                //不换行
                horizontalWidth += childWidth
                maxLineHeight = if (maxLineHeight > childHeight) maxLineHeight else childHeight
            }
        }

        resultWidth = if (widthMode == MeasureSpec.AT_MOST) maxLineWidth else widthSize
        resultHeight = if (heightMode == MeasureSpec.AT_MOST) resultHeight else heightSize

        setMeasuredDimension(resultWidth, resultHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

    }
}