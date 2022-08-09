package com.pisto.sqlite.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pisto.sqlite.R
import com.pisto.sqlite.entity.StudentInfo

class StudentInfoAdapter(val infoList: MutableList<StudentInfo> = mutableListOf()) : RecyclerView.Adapter<VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_student_info, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val studentInfo = infoList[position]
        holder.tvName.text = studentInfo.name
        holder.tvStudentNumber.text = studentInfo.studentNumber
        holder.tvGender.text = if (studentInfo.gender == 0) {
            "男"
        } else {
            "女"
        }
        holder.tvBirthday.text = studentInfo.birthday
        holder.tvPhone.text = studentInfo.phone
        holder.tvAddress.text = studentInfo.address
    }

    override fun getItemCount(): Int {
        return infoList.size
    }
}

class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName: TextView = itemView.findViewById(R.id.tv_name)
    val tvStudentNumber: TextView = itemView.findViewById(R.id.tv_student_number)
    val tvGender: TextView = itemView.findViewById(R.id.tv_gender)
    val tvBirthday: TextView = itemView.findViewById(R.id.tv_birthday)
    val tvPhone: TextView = itemView.findViewById(R.id.tv_phone)
    val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
}