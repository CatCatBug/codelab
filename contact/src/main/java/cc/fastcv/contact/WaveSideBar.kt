package cc.fastcv.contact

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class WaveSideBar : View {

    companion object {
        private const val TAG = "WaveSideBar"

        private val DEFAULT_INDEX_ITEMS = arrayOf(
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"
        )
    }



    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context,attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context,attributeSet)
    }

    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attributeSet, defStyleAttr, defStyleRes) {
        init(context,attributeSet)
    }

    private lateinit var paint : Paint
    private var measureText : Float = 0f
    private var drawY : Float = 0f
    private var drawX : Float = 0f

    private fun init(context: Context, attributeSet: AttributeSet?) {
        paint = Paint()
        paint.color = Color.YELLOW
        paint.textSize = 40f
        val fontMetrics = paint.fontMetrics
        Log.d(TAG, "descent: ${fontMetrics.descent}")
        Log.d(TAG, "ascent: ${fontMetrics.ascent}")
        Log.d(TAG, "bottom: ${fontMetrics.bottom}")
        Log.d(TAG, "top: ${fontMetrics.top}")
        Log.d(TAG, "leading: ${fontMetrics.leading}")
        Log.d(TAG, "lineHeight: ${fontMetrics.bottom - fontMetrics.top}")
        Log.d(TAG, "baseLine: ${fontMetrics.bottom - fontMetrics.top}")
        measureText = paint.measureText("abcdefghijklmnopqrstuvwxyz123456789这是一段文字")
        Log.d(TAG, "measureText: $measureText")
        drawY = -paint.fontMetrics.ascent
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val fontMetrics = paint.fontMetrics
        setMeasuredDimension(widthMeasureSpec,MeasureSpec.makeMeasureSpec((fontMetrics.descent - fontMetrics.ascent).toInt(),MeasureSpec.EXACTLY))
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.drawText("abcdefghijklmnopqrstuvwxyz123456789这是一段文字",(width - measureText)/2 + drawX,drawY,paint)
    }
}