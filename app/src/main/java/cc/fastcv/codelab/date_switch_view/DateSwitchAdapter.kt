package cc.fastcv.codelab.date_switch_view

import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import cc.fastcv.codelab.R

class DateSwitchAdapter(val list: MutableList<WeekInfo>) : BaseRecyclerViewAdapter<WeekInfo>() {
    private var lastDayInfo: DayInfo? = null

    init {
        for (weekInfo in list) {
            for (dayInfo in weekInfo.weekContent) {
                if (dayInfo.isToday) {
                    lastDayInfo = dayInfo
                    break
                }
            }

            if (lastDayInfo != null) {
                break
            }
        }
    }

    private var itemClickListener: OnItemClickListener<DayInfo>? = null

    override fun getLayoutId(viewType: Int): Int = R.layout.item_step_day

    override fun getDataByPosition(position: Int): WeekInfo = list[position]

    override fun getTotalSize(): Int = list.size

    override fun convert(holder: BaseViewHolder, data: WeekInfo, position: Int) {
        holder.itemView.setOnClickListener(null)
        data.weekContent.forEachIndexed { index, dayInfo ->
            val textId = getTextId(index)
            holder.getView<TextView>(textId).text =  if (dayInfo.type == 0) {
                dayInfo.day.toString()
            } else {
                dayInfo.month.toString()
            }

            if (dayInfo.isToday) {
                holder.getView<TextView>(textId).setTextColor(Color.parseColor("#FFFFFF"))
                holder.getView<TextView>(textId).setBackgroundResource(R.drawable.day_today_bg)
            }else{
                holder.getView<TextView>(textId).setTextColor(Color.parseColor("#555555"))
                holder.getView<TextView>(textId).setBackgroundResource(0)
            }

            if (dayInfo.isSelected) {
                holder.getView<TextView>(textId).setTextColor(Color.parseColor("#FFFFFF"))
                holder.getView<TextView>(textId).setBackgroundResource(R.drawable.day_select_bg)
            }

            if (dayInfo.isInvalid) {
                holder.getView<TextView>(textId).setTextColor(Color.parseColor("#d6d6d6"))
                holder.getView<AppCompatTextView>(textId).setOnClickListener(null)
            } else {
                val pos = holder.layoutPosition

                holder.getView<AppCompatTextView>(textId).setOnClickListener{
                    if (itemClickListener != null) {
                        //取消上次的点击位置
                        if (lastDayInfo != null) {
                            lastDayInfo!!.isSelected = false
                        }
                        dayInfo.isSelected = true
                        lastDayInfo = dayInfo
                        itemClickListener!!.onItemClick(
                            holder.itemView,
                            pos,
                            dayInfo
                        )
                    }
                }
            }
        }
    }

    private fun getTextId(index : Int) : Int{
        return when (index) {
            0 -> R.id.sunday_tv
            1 -> R.id.monday_tv
            2 -> R.id.tuesday_tv
            3 -> R.id.wed_tv
            4 -> R.id.thursday_tv
            5 -> R.id.friday_tv
            else -> R.id.sat_tv
        }
    }

    fun setItemClickListener(listener: OnItemClickListener<DayInfo>) {
        itemClickListener = listener
    }
}