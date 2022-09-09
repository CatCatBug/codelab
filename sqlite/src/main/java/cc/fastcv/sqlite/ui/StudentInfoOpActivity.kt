package cc.fastcv.sqlite.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.sqlite.R
import cc.fastcv.sqlite.db.Database

class StudentInfoOpActivity : AppCompatActivity() {

    companion object {
        fun intoActivity(context: Context) {
            context.startActivity(Intent(context, StudentInfoOpActivity::class.java))
        }
    }

    private lateinit var adapter: StudentInfoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_info_op)
        initView()
        initListener()
    }

    private fun initListener() {
        findViewById<ImageView>(R.id.iv_add).setOnClickListener {
            StudentInfoEditActivity.intoActivity(this)
        }

        findViewById<TextView>(R.id.tv_query).setOnClickListener {
            queryInfo()
        }
    }

    private fun initView() {
        initListView()
    }

    private fun initListView() {
        val rvInfo = findViewById<RecyclerView>(R.id.rv_info)
        adapter = StudentInfoAdapter()
        rvInfo.adapter = adapter
    }


    @SuppressLint("NotifyDataSetChanged")
    fun queryInfo() {
        val studentNumber = getFilterStudentNumber()
        val gender = getFilterGender()
        val text = getFilterText()
        val textType = getFilterTextType()
        val year = getFilterYear()
        val month = getFilterMonth()
        val day = getFilterDay()

        val endYear = getFilterYearEnd()
        val endMonth = getFilterMonthEnd()
        val endDay = getFilterDayEnd()

        val startYear = getFilterRegisterYear()
        val phone = getFilterPhone()
        val queryAll = Database.getStudentInfoDao()
            .queryAll(studentNumber, gender, text, textType, year, month, day, endYear, endMonth, endDay, startYear, phone)
        adapter.infoList.clear()
        adapter.infoList.addAll(queryAll)
        adapter.notifyDataSetChanged()

    }

    private fun getFilterPhone(): String? {
        val trim = findViewById<EditText>(R.id.et_filter_phone).text.toString().trim()
        return if (TextUtils.isEmpty(trim)) {
            null
        } else {
            trim
        }
    }

    private fun getFilterRegisterYear(): String? {
        val year = findViewById<Spinner>(R.id.spinner_filter_start_year).selectedItemPosition
        return if (year == 0) {
            null
        } else {
            resources.getStringArray(R.array.filter_year)[year]
        }
    }

    private fun getFilterDayEnd(): Int? {
        val day = findViewById<Spinner>(R.id.spinner_filter_day_end).selectedItemPosition
        return if (day == 0) {
            null
        } else {
            day
        }
    }

    private fun getFilterMonthEnd(): Int? {
        val month = findViewById<Spinner>(R.id.spinner_filter_month_end).selectedItemPosition
        return if (month == 0) {
            null
        } else {
            month
        }
    }

    private fun getFilterYearEnd(): Int? {
        val yearIndex = findViewById<Spinner>(R.id.spinner_filter_year_end).selectedItemPosition
        return if (yearIndex == 0) {
            null
        } else {
            resources.getStringArray(R.array.filter_year)[yearIndex].toIntOrNull()
        }
    }

    private fun getFilterDay(): Int? {
        val day = findViewById<Spinner>(R.id.spinner_filter_day).selectedItemPosition
        return if (day == 0) {
            null
        } else {
            day
        }
    }

    private fun getFilterMonth(): Int? {
        val month = findViewById<Spinner>(R.id.spinner_filter_month).selectedItemPosition
        return if (month == 0) {
            null
        } else {
            month
        }
    }

    private fun getFilterYear(): Int? {
        val yearIndex = findViewById<Spinner>(R.id.spinner_filter_year).selectedItemPosition
        return if (yearIndex == 0) {
            null
        } else {
            resources.getStringArray(R.array.filter_year)[yearIndex].toIntOrNull()
        }
    }


    private fun getFilterTextType(): Int {
        return findViewById<Spinner>(R.id.spinner_filter_text).selectedItemPosition
    }

    private fun getFilterText(): String? {
        val trim = findViewById<EditText>(R.id.et_filter).text.toString().trim()
        return if (TextUtils.isEmpty(trim)) {
            null
        } else {
            trim
        }
    }

    //2020 01658129611742
    private fun getFilterGender(): Int? {
        val gender = findViewById<Spinner>(R.id.spinner_filter_gender).selectedItemPosition
        return if (gender == 0) {
            null
        } else {
            gender - 1
        }
    }

    private fun getFilterStudentNumber(): String? {
        val trim = findViewById<EditText>(R.id.et_student_number).text.toString().trim()
        return if (TextUtils.isEmpty(trim)) {
            null
        } else {
            trim
        }
    }
}