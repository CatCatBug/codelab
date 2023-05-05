package cc.fastcv.sqlite.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import cc.fastcv.sqlite.entity.StudentInfo

class StudentInfoDao(
    private val readDatabase: SQLiteDatabase,
    private val writeDatabase: SQLiteDatabase
) {
    companion object {
        private const val TAG = "StudentInfoDao"
    }

    fun insert(studentInfo: StudentInfo) {
        //创建存放数据的ContentValues对象
        val values = ContentValues()
        values.put("student_number", studentInfo.studentNumber)
        values.put("name", studentInfo.name)
        values.put("gender", studentInfo.gender)
        values.put("phone", studentInfo.phone)
        values.put("birthday", studentInfo.birthday)
        values.put("address", studentInfo.address)
        //数据库执行插入命令
        //插入成功就返回记录的id否则返回-1
        val insert = writeDatabase.insert("student_info", null, values)
        Log.d(TAG, "insert: $insert")
    }


    fun queryAll(
        studentNumber: String?,
        gender: Int?,
        text: String?,
        textType: Int,
        year: Int?,
        month: Int?,
        day: Int?,
        endYear: Int?,
        endMonth: Int?,
        endDay: Int?,
        startYear: String?,
        phone: String?
    ) : List<StudentInfo> {
        //生成过滤语句
        val selectionBuild = StringBuilder()
        val selectionArgs = mutableListOf<String>()

        //判断是否需要过滤学号
        if (!TextUtils.isEmpty(studentNumber)) {
            selectionBuild.append("student_number = ? and ")
            selectionArgs.add(studentNumber!!)
        }

        //判断是否需要过滤性别
        if (gender != null) {
            selectionBuild.append("gender = ? and ")
            selectionArgs.add(gender.toString())
        }

        //判断是否需要模糊查询
        //模糊查询有三种方式 此处是第一种，第二种在下面，第三种就是硬编码写SQL语句
        if (!TextUtils.isEmpty(text)) {
            if (textType == 0) {
                //名字过滤
                selectionBuild.append("name LIKE ? and ")
            } else {
                //地址过滤
                selectionBuild.append("address LIKE ? and ")
            }
            selectionArgs.add("%" + text!! + "%")
        }

        //生日判断

        val filterBirthSelection = buildSelection(year,endYear,month,endMonth,day,endDay)


        if (!TextUtils.isEmpty(filterBirthSelection)) {
            val filterBirthArgs = buildSelectionArgs(year,endYear,month,endMonth,day,endDay)
            selectionBuild.append(filterBirthSelection)
            selectionArgs.addAll(filterBirthArgs)
        }

        //select * from mytable where strftime('%Y-%m-%d', datatime) = '2017-05-10'
        //select * from mytable where strftime('%m', datatime) = '05'
        //入学年份查询
        //判断是否需要模糊查询
        //此处就是第二种模糊查询的方式
        if (!TextUtils.isEmpty(startYear)) {
            selectionBuild.append("student_number LIKE '%" + startYear!! +  "%' and ")
        }

        //电话号码查询
        if (!TextUtils.isEmpty(phone)) {
            selectionBuild.append("phone = ? and ")
            selectionArgs.add(phone!!)
        }

        var filterResult = selectionBuild.toString()
        val selectionStr = if (!TextUtils.isEmpty(filterResult)) {
            filterResult.substring(0,filterResult.length-4)
        } else {
            null
        }

        Log.d(TAG, "queryAll: selection = $selectionStr")
        Log.d(TAG, "queryAll: selectionArgs = ${selectionArgs.joinToString()}")

        val queryAll = queryAll(selectionStr, selectionArgs.toTypedArray(), null, null, null)
        Log.d(TAG, "queryAll: size = ${queryAll.size}")
        return queryAll

    }

    private fun buildSelectionArgs(
        year: Int?,
        endYear: Int?,
        month: Int?,
        endMonth: Int?,
        day: Int?,
        endDay: Int?
    ): Array<String> {
        //首先判断是范围查询还是日期查询
        val isRangeQuery = (year != null && endYear != null) ||
                (month != null && endMonth != null) ||
                (day != null && endDay != null)

        if (isRangeQuery) {
            //是范围查询
            var resultDate = ""
            var resultDateEnd = ""

            if (year != null && endYear != null) {
                resultDate += "$year-"
                resultDateEnd += "$endYear-"
            }

            if (month != null && endMonth != null) {
                resultDate += "${String.format("%02d",month)}-"
                resultDateEnd += "${String.format("%02d",endMonth)}-"
            }

            if (day != null && endDay != null) {
                resultDate += "${String.format("%02d",day)}-"
                resultDateEnd += "${String.format("%02d",endDay)}-"
            }

            return if (TextUtils.isEmpty(resultDate)) {
                arrayOf("")
            } else {
                return arrayOf(resultDate.substring(0, resultDate.length-1),resultDateEnd.substring(0, resultDateEnd.length-1))
            }
        } else {
            //日期查询
            if (year != null || month != null || day != null) {
                var resultDate = ""

                if (year != null) {
                    resultDate += "$year-"
                }

                if (month != null) {
                    resultDate += "${String.format("%02d",month)}-"
                }

                if (day != null) {
                    resultDate += "${String.format("%02d",day)}-"
                }

                return arrayOf(resultDate.substring(0, resultDate.length-1))
            } else {
                return arrayOf("")
            }
        }
    }

    private fun buildSelection(
        year: Int?,
        endYear: Int?,
        month: Int?,
        endMonth: Int?,
        day: Int?,
        endDay: Int?
    ): String {
        //首先判断是范围查询还是日期查询
        val isRangeQuery = (year != null && endYear != null) ||
                (month != null && endMonth != null) ||
                (day != null && endDay != null)

        if (isRangeQuery) {
            //是范围查询
            var filterDate = ""

            if (year != null && endYear != null) {
                filterDate += "%Y-"
            }

            if (month != null && endMonth != null) {
                filterDate += "%m-"
            }

            if (day != null && endDay != null) {
                filterDate += "%d-"
            }
            return if (TextUtils.isEmpty(filterDate)) {
                ""
            } else {
                "strftime('"  + filterDate.substring(0, filterDate.length - 1)+  "',birthday) BETWEEN ? AND ? and "
            }
        } else {
            //日期查询
            if (year != null || month != null || day != null) {
                var filterDate = ""

                if (year != null) {
                    filterDate += "%Y-"
                }

                if (month != null) {
                    filterDate += "%m-"
                }

                if (day != null) {
                    filterDate += "%d-"
                }

                return "strftime('"  + filterDate.substring(0, filterDate.length - 1)+  "',birthday) = ? and "
            } else {
                return ""
            }
        }
    }

    private fun queryAll(
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): List<StudentInfo> {
        Log.d(TAG, "queryAll: selection = $selection   size = ${selectionArgs?.size}")
        val infoList = mutableListOf<StudentInfo>()

        //创建游标对象
        val cursor = readDatabase.query(
            "student_info",
            arrayOf(
                "id",
                "student_number",
                "name",
                "gender",
                "phone",
                "birthday",
                "address"
            ),
            selection,
            selectionArgs,
            groupBy,
            having,
            orderBy
        )
        //利用游标遍历所有数据对象
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex("id").minus(0))
            val studentNumber = cursor.getString(cursor.getColumnIndex("student_number").minus(0))
            val name = cursor.getString(cursor.getColumnIndex("name").minus(0))
            val gender = cursor.getInt(cursor.getColumnIndex("gender").minus(0))
            val phone = cursor.getString(cursor.getColumnIndex("phone").minus(0))
            val birthday = cursor.getString(cursor.getColumnIndex("birthday").minus(0))
            val address = cursor.getString(cursor.getColumnIndex("address").minus(0))

            val studentInfo = StudentInfo(id, studentNumber, name, gender, phone, birthday, address)
            infoList.add(studentInfo)
        }
        // 关闭游标，释放资源
        cursor.close()
        return infoList
    }


    fun delete(studentInfo: StudentInfo) {
        //根据id删除
        val delete = writeDatabase.delete("student_info", "id=?", arrayOf(studentInfo.id.toString()))
        Log.d(TAG, "delete: $delete")
    }

    fun update(studentInfo: StudentInfo) {
        //根据id修改描述
        val values = ContentValues()
        values.put("student_number", studentInfo.studentNumber)
        values.put("name", studentInfo.name)
        values.put("gender", studentInfo.gender)
        values.put("phone", studentInfo.phone)
        values.put("birthday", studentInfo.birthday)
        values.put("address", studentInfo.address)
        val update = writeDatabase.update("student_info", values, "id=?", arrayOf(studentInfo.id.toString()))
        Log.d(TAG, "update: $update")
    }
}