package cc.fastcv.contact

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

class ContactsAdapter(private val contacts: List<Contact>, private val layoutId: Int) : RecyclerView.Adapter<ContactsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutId, null)
        return ContactsViewHolder(view)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        if (position == 0 || contacts[position - 1].index != contact.index) {
            holder.llGroup.visibility = View.VISIBLE
            holder.tvIndex.text = contact.index
        } else {
            holder.llGroup.visibility = View.GONE
        }

        holder.tvName.text = contact.name
        holder.tvPhone.text = contact.phone

        holder.ivNameIndex.apply {
            text = contact.name.subSequence(0,1)
            background = context.getDrawable(R.drawable.contact_circle_bg).apply {
                this?.setTint(Color.YELLOW)
            }
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }
}

class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvIndex: TextView = itemView.findViewById(R.id.tv_index)
    var llGroup: LinearLayout = itemView.findViewById(R.id.ll_group)
    var ivNameIndex: TextView = itemView.findViewById(R.id.iv_name_index)
    var tvName: TextView = itemView.findViewById(R.id.tv_name)
    var tvPhone: TextView = itemView.findViewById(R.id.tv_phone)
    var cbCheck: CheckBox = itemView.findViewById(R.id.cb_check)

}