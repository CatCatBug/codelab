package cc.fastcv.ui.rolling_counter

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * 支持long类型的数值滚动
 */
class RollingCounterView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    /**
     * 行高
     */
    private var mLineHeight = 0f

    /**
     * 字体大小
     */
    private var mTextSize = 100f

    /**
     * 字体颜色
     */
    private var mTextColor = Color.BLACK

    /**
     * MAX Vaulue
     */
    private val max = Long.MAX_VALUE

    /**
     * 上次值
     */
    private var lastValue = 0L

    /**
     * 当前值
     */
    private var curValue = 0L

    /**
     * 文字画笔
     */
    private lateinit var mPaint: Paint

    /**
     * 文字基线
     */
    private var mBaseLine = 0f

    /**
     * 数字中最大的字宽
     */
    private var mMaxWidth = 0f

    /**
     * 平移Y
     */
    private var translationYValue = 0f

    private var isAnimating = false


    private var targetValue = 0L

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Thread.dumpStack()
    }


    fun plus(value: Int = 1) {
        targetValue += value
        if (isAnimating) return

        lastValue = curValue
        curValue += targetValue
        targetValue = 0L
        startAnim()
    }

    private fun startAnim() {
        val ofFloat = ValueAnimator.ofFloat(0f, (curValue - lastValue).toFloat())
        ofFloat.duration = 250L
        ofFloat.addUpdateListener {
            val value = it.animatedValue as Float
            translationYValue = -mLineHeight * value
            invalidate()
        }
        ofFloat.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                isAnimating = true
            }

            override fun onAnimationEnd(animation: Animator?) {
                if (targetValue != 0L) {
                    lastValue = curValue
                    curValue += targetValue
                    targetValue = 0L
                    startAnim()
                } else {
                    isAnimating = false
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        ofFloat.start()
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        mPaint = Paint()
        mPaint.textSize = mTextSize
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.color = mTextColor

        val fontMetrics = mPaint.fontMetrics

        mBaseLine = -fontMetrics.top

        mLineHeight = fontMetrics.bottom - fontMetrics.top
        for (i in 0..9) {
            mMaxWidth = max(mPaint.measureText(i.toString()), mMaxWidth)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (lastValue == curValue) {
            canvas?.drawText(
                curValue.toString(),
                (width / 2).toFloat(),
                (height / 2).toFloat() + mBaseLine - mLineHeight / 2 + translationYValue,
                mPaint
            )
        } else {
            for (i in 0..(curValue - lastValue)) {
                canvas?.drawText(
                    (i + lastValue).toString(),
                    (width / 2).toFloat(),
                    (height / 2).toFloat() + mBaseLine - mLineHeight / 2 + translationYValue + mLineHeight * i,
                    mPaint
                )
            }
//            canvas?.drawText(lastValue.toString(),
//                (width/2).toFloat(), (height/2).toFloat() + mBaseLine - mLineHeight/2 + translationYValue,mPaint)

        }
    }


}