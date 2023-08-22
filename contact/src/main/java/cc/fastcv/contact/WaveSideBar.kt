package cc.fastcv.contact

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

open class WaveSideBar : View {

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

    private val mIndexItems: Array<String> = DEFAULT_INDEX_ITEMS

    private lateinit var mPaint : Paint

    private var mItemHeight = 0.0f
    private var mBarHeight = 0.0f
    private var mBarWidth = 0.0f

    private val touchArea = RectF()

    private var mFirstItemBaseLineY = 0f
    private var mBaseLineX = 0f

    /**
     * if true, the [OnSelectIndexItemListener.onSelectIndexItem]
     * will not be called until the finger up.
     * if false, it will be called when the finger down, up and move.
     */
    private val mLazyRespond = false

    /**
     * observe the current selected index item
     */
    private var onSelectIndexItemListener: OnSelectIndexItemListener? = null

    private fun init(context: Context, attributeSet: AttributeSet?) {
        mPaint = Paint()
        mPaint.color = Color.BLACK
        mPaint.textSize = 40f
        mPaint.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        //获取单个字符的高度
        val fontMetrics = mPaint.fontMetrics
        mItemHeight = fontMetrics.bottom - fontMetrics.top
        //最小总高度
        mBarHeight = mIndexItems.size * mItemHeight

        //获取字符中最大宽度,即最小宽度
        for (indexItem in mIndexItems) {
            mBarWidth = mBarWidth.coerceAtLeast(mPaint.measureText(indexItem))
        }

        //得到事件处理区域  我们支持在右边显示
        val touchAreaLeft = width - mBarWidth - paddingEnd - mBarWidth
        val touchAreaRight = width.toFloat()
        val touchAreaTop = (height - mBarHeight)/2
        val touchAreaBottom = touchAreaTop + mBarHeight
        touchArea.set(touchAreaLeft,touchAreaTop,touchAreaRight,touchAreaBottom)
        //获取第一个文字绘制的Y轴
        mFirstItemBaseLineY = touchAreaTop - fontMetrics.top

        //通用的X轴
        mBaseLineX = touchAreaLeft - 20 + mBarWidth

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        mIndexItems.forEachIndexed { index, s ->
            canvas?.drawText(s,mBaseLineX ,mFirstItemBaseLineY + mItemHeight*index,mPaint)
        }
    }

    var lastIndex = -1
    var mStartTouching = false

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //如果数据为空 则不处理任何事件
        if (mIndexItems.isEmpty()) {
            return super.onTouchEvent(event)
        }

        //获取触摸点
        val eventY = event!!.y
        val eventX = event.x

        val mCurrentIndex = getPositionByPoint(eventY)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> return if (touchArea.contains(eventX, eventY)) {
                mStartTouching = true
                lastIndex = mCurrentIndex
                if (!mLazyRespond && onSelectIndexItemListener != null) {
                    onSelectIndexItemListener!!.onSelectIndexItem(mIndexItems[mCurrentIndex])
                }
                invalidate()
                true
            } else {
                false
            }
            MotionEvent.ACTION_MOVE -> {
                if (lastIndex != mCurrentIndex) {
                    lastIndex = mCurrentIndex
                }
                if (mStartTouching && !mLazyRespond && onSelectIndexItemListener != null) {
                    onSelectIndexItemListener!!.onSelectIndexItem(mIndexItems[mCurrentIndex])
                }
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (lastIndex != mCurrentIndex) {
                    lastIndex = mCurrentIndex
                }
                if (mLazyRespond && onSelectIndexItemListener != null) {
                    onSelectIndexItemListener!!.onSelectIndexItem(mIndexItems[mCurrentIndex])
                }
                invalidate()
                return true
            }
        }

        return super.onTouchEvent(event)
    }

    //解析触摸点所在位置
    open fun getPositionByPoint(eventY: Float): Int {
        val dY = eventY - (height - mBarHeight) / 2
        if (dY <= 0) {
            return 0
        }
        //判断处于哪个位置
        var index = (dY / mItemHeight).toInt()
        if (index >= mIndexItems.size) {
            index = mIndexItems.size - 1
        }
        return index
    }

    fun setOnSelectIndexItemListener(onSelectIndexItemListener: OnSelectIndexItemListener) {
        this.onSelectIndexItemListener = onSelectIndexItemListener
    }
}

interface OnSelectIndexItemListener {
    fun onSelectIndexItem(index: String?)
}