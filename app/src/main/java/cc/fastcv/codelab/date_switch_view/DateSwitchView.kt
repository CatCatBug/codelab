package cc.fastcv.codelab.date_switch_view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.codelab.R

class DateSwitchView : FrameLayout {
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }

    companion object {
        private const val TYPE_DAY = 0
        private const val TYPE_MONTH = 1
    }

    private lateinit var dayBtn: TextView
    private lateinit var monthBtn: TextView
    private lateinit var rvDate: RecyclerView
    private lateinit var adapter: DateSwitchAdapter
    private lateinit var tvDate: TextView
    private lateinit var viewSwitchBg: View

    private var callback: DateSelectCallback? = null

    private val dateBuildFactory = DateBuildFactory()

    private val toggleAnimation: ValueAnimator by lazy {
        val toggleAnimation =
            ValueAnimator.ofFloat(
                dip2px(40, context),
                kotlin.math.abs(monthBtn.x - dayBtn.x) + dip2px(40, context)
            )
        toggleAnimation.duration = 250
        toggleAnimation.addUpdateListener {
            val layoutParams = viewSwitchBg.layoutParams as LayoutParams
            layoutParams.marginStart = (it.animatedValue as Float).toInt()
            viewSwitchBg.layoutParams = layoutParams
        }
        toggleAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {

                Log.d("xcl_debug", "onAnimationEnd: $targetType")
                if (curDateType != targetType) {
                    curDateType = targetType
                    if (curDateType == TYPE_DAY) {
                        switchToDayType()
                    } else {
                        switchToMonthType()
                    }
                }
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        toggleAnimation
    }

    fun setDateSelectCallback(callback: DateSelectCallback) {
        this.callback = callback
    }

    private var curDateType = TYPE_DAY

    private var targetType = TYPE_DAY

    private fun initView(context: Context) {
        inflate(context, R.layout.data_switch_view, this)
        initBtView()
        initDateRv()
    }

    private fun initDateRv() {
        rvDate = findViewById(R.id.rv_date)

        adapter = DateSwitchAdapter(mutableListOf())
        rvDate.adapter = adapter
        adapter.setItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<DayInfo> {
            override fun onItemClick(view: View?, position: Int, t: DayInfo) {
                callback?.onDateSelect(t)
            }
        })
        val mCardScaleHelper = CardScaleHelper()
        mCardScaleHelper.attachToRecyclerView(rvDate)
    }


    private fun initBtView() {
        tvDate = findViewById(R.id.tv_date)
        viewSwitchBg = findViewById(R.id.view_switch_bg)

        dayBtn = findViewById(R.id.day_btn)
        dayBtn.setOnClickListener {
            startToggleAnimation(TYPE_DAY)
        }
        monthBtn = findViewById(R.id.month_btn)
        monthBtn.setOnClickListener {
            startToggleAnimation(TYPE_MONTH)
        }
    }

    private fun startToggleAnimation(it: Int) {
        targetType = it
        if (it == 1) {
            //切换到月
            toggleAnimation.start()
        } else {
            //切换到日
            toggleAnimation.reverse()
        }
    }

    private fun switchToMonthType() {
        if (!TextUtils.isEmpty(dateBuildFactory.startDate)) {
            tvDate.text = dateBuildFactory.endDate.split("-")[0]

            //更新颜色
            dayBtn.setTextColor(Color.parseColor("#ACACAC"))
            monthBtn.setTextColor(Color.parseColor("#FFFFFF"))

            val list = dateBuildFactory.buildMonthList()
            adapter = DateSwitchAdapter(list)
            rvDate.adapter = adapter
            adapter.setItemClickListener(object :
                BaseRecyclerViewAdapter.OnItemClickListener<DayInfo> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClick(view: View?, position: Int, t: DayInfo) {
                    selectDate(t)
                    adapter.notifyDataSetChanged()
                }
            })
            callback?.onDateSelect(dateBuildFactory.buildInitDate(curDateType))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun switchToDayType() {
        if (!TextUtils.isEmpty(dateBuildFactory.startDate)) {
            tvDate.text = "${getMonthStr(dateBuildFactory.endDate.split("-")[1].toInt())} · ${
                dateBuildFactory.endDate.split("-")[0]
            }"

            //更新颜色
            dayBtn.setTextColor(Color.parseColor("#FFFFFF"))
            monthBtn.setTextColor(Color.parseColor("#ACACAC"))

            val list = dateBuildFactory.buildDayList()
            adapter = DateSwitchAdapter(list)
            rvDate.adapter = adapter
            adapter.setItemClickListener(object :
                BaseRecyclerViewAdapter.OnItemClickListener<DayInfo> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onItemClick(view: View?, position: Int, t: DayInfo) {
                    selectDate(t)
                    adapter.notifyDataSetChanged()
                }
            })
            callback?.onDateSelect(dateBuildFactory.buildInitDate(curDateType))
        }
    }


    private fun getMonthStr(month: Int): String {
        return when (month) {
            1 -> context.getString(R.string.common_jan)
            2 -> context.getString(R.string.common_feb)
            3 -> context.getString(R.string.common_mar)
            4 -> context.getString(R.string.common_apr)
            5 -> context.getString(R.string.common_may)
            6 -> context.getString(R.string.common_jun)
            7 -> context.getString(R.string.common_jul)
            8 -> context.getString(R.string.common_aug)
            9 -> context.getString(R.string.common_sep)
            10 -> context.getString(R.string.common_oct)
            11 -> context.getString(R.string.common_nov)
            12 -> context.getString(R.string.common_dec)
            else -> context.getString(R.string.common_jan)
        }
    }


    interface DateSelectCallback {
        fun onDateSelect(info: DayInfo)
    }


    /**
     * 格式：yyyy-MM-dd
     */
    fun setDateRange(start: String, end: String) {
        dateBuildFactory.setDateRange(start, end)
        tvDate.text = if (curDateType == TYPE_DAY) {
            "${getMonthStr(end.split("-")[1].toInt())} · ${end.split("-")[0]}"
        } else {
            end.split("-")[0]
        }
        val list = if (curDateType == TYPE_DAY) {
            dateBuildFactory.buildDayList()
        } else {
            dateBuildFactory.buildMonthList()
        }
        adapter = DateSwitchAdapter(list)
        rvDate.adapter = adapter
        adapter.setItemClickListener(object :
            BaseRecyclerViewAdapter.OnItemClickListener<DayInfo> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(view: View?, position: Int, t: DayInfo) {
                selectDate(t)
                adapter.notifyDataSetChanged()
            }
        })
        callback?.onDateSelect(dateBuildFactory.buildInitDate(curDateType))
    }

    fun selectDate(dayInfo: DayInfo) {
        //更新日期文本
        tvDate.text = if (dayInfo.type == 0) {
            "${getMonthStr(dayInfo.month)} · ${dayInfo.year}"
        } else {
            "${dayInfo.year}"
        }
        //回调接口
        callback?.onDateSelect(dayInfo)
    }


    private fun dip2px(dpValue: Int, context: Context): Float {
        val density = context.resources.displayMetrics.density
        return dpValue.toFloat() * density
    }
}