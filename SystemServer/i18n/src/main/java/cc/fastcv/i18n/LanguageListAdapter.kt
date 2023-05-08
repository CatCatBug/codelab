package cc.fastcv.i18n

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class LanguageListAdapter(private val languageList: List<LanguageItem>) : RecyclerView.Adapter<VH>() {

    private var itemClickListener:ItemClickListener? = null

    fun setOnItemClickListener(listener:ItemClickListener?) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.tvLanguage.text = languageList[position].descript
        holder.ivCheck.setImageDrawable(
            if (languageList[position].checked) {
                ContextCompat.getDrawable(holder.ivCheck.context,R.drawable.ic_check)
            } else {
                null
            }
        )
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClickListener(languageList[position],position)
        }
    }

    override fun getItemCount(): Int = languageList.size

    fun chooseLanguageItem(data: LanguageItem) {
        languageList.forEach {
            it.checked = it == data
        }
        notifyItemRangeChanged(0,languageList.size)
    }

    fun getSelectIndex() : Int {
        languageList.forEachIndexed { index, languageItem ->
            if (languageItem.checked) {
                return index
            }
        }
        return -1
    }
}

interface ItemClickListener {
    fun onItemClickListener(data:LanguageItem, position: Int)
}

class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvLanguage: TextView = itemView.findViewById(R.id.tv_language)
    var ivCheck: ImageView = itemView.findViewById(R.id.iv_check)
}