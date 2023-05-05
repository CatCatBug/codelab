package cc.fastcv.filemanager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileAdapter(val list: MutableList<File>) : RecyclerView.Adapter<VH>() {

    var itemClickListener: OnItemClickListener<File>? = null
    var itemLongClickListener: OnItemLongClickListener<File>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_file,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: VH, position: Int) {
        if (list[position].isDirectory) {
            holder.ivIcon.setImageResource(R.drawable.ic_folder)
            holder.itemView.setOnClickListener{
                itemClickListener?.onItemClickListener(list[position],position)
            }
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_file)
            holder.itemView.setOnClickListener(null)
        }
        holder.tvTitle.text = "${list[position].name }    ${list[position].canRead()}/${list[position].canWrite()}"
        holder.itemView.setOnLongClickListener{
            itemLongClickListener?.onItemLongClickListener(list[position],position)?:false
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
    val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
}

interface OnItemClickListener<T> {
    fun onItemClickListener(t: T,position: Int)
}

interface OnItemLongClickListener<T> {
    fun onItemLongClickListener(t: T,position: Int):Boolean
}