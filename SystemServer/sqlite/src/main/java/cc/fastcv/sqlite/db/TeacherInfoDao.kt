package cc.fastcv.sqlite.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import cc.fastcv.sqlite.entity.TeacherInfo

class TeacherInfoDao(
    private val readDatabase: SQLiteDatabase,
    private val writeDatabase: SQLiteDatabase
) {
    companion object {
        private const val TAG = "TeacherInfoDao"
    }

    fun insert(teacherInfo: TeacherInfo) {
        //创建存放数据的ContentValues对象
        val values = ContentValues()
        values.put("teacher_number", teacherInfo.teacherNumber)
        values.put("name", teacherInfo.name)
        values.put("gender", teacherInfo.gender)
        values.put("phone", teacherInfo.phone)
        values.put("birthday", teacherInfo.birthday)
        values.put("address", teacherInfo.address)
        //数据库执行插入命令
        //插入成功就返回记录的id否则返回-1
        val insert = writeDatabase.insert("teacher_info", null, values)
        Log.d(TAG, "insert: $insert")
    }


    fun queryAll(
        teacherNumber: String?,
        gender: Int?,
        text: String?,
        textType: Int,
        year: Int?,
        month: Int?,
        day: Int?,
        startYear: String?,
        phone: String?
    ) : List<TeacherInfo> {
        //生成过滤语句
        val selectionBuild = StringBuilder()
        val selectionArgs = mutableListOf<String>()

        //判断是否需要过滤学号
        if (!TextUtils.isEmpty(teacherNumber)) {
            selectionBuild.append("teacher_number = ? and ")
            selectionArgs.add(teacherNumber!!)
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
        if (year != null || month != null || day != null) {
            var filterDate = ""
            var resultDate = ""

            if (year != null) {
                filterDate += "%Y-"
                resultDate += "$year-"
            }

            if (month != null) {
                filterDate += "%m-"
                resultDate += "${String.format("%02d",month)}-"
            }

            if (day != null) {
                filterDate += "%d-"
                resultDate += "${String.format("%02d",day)}-"
            }
            Log.d(TAG, "queryAll: resultDate = $resultDate")
            selectionBuild.append("strftime('"  + filterDate.substring(0, filterDate.length - 1)+  "',birthday) = ? and ")
            selectionArgs.add(resultDate.substring(0, resultDate.length-1))
        }

        //select * from mytable where strftime('%Y-%m-%d', datatime) = '2017-05-10'
        //select * from mytable where strftime('%m', datatime) = '05'
        //入学年份查询
        //判断是否需要模糊查询
        //此处就是第二种模糊查询的方式
        if (!TextUtils.isEmpty(startYear)) {
            selectionBuild.append("teacher_number LIKE '%" + startYear!! +  "%' and ")
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

    private fun queryAll(
        selection: String?,
        selectionArgs: Array<String>?,
        groupBy: String?,
        having: String?,
        orderBy: String?
    ): List<TeacherInfo> {


        Log.d(TAG, "queryAll: selection = $selection   size = ${selectionArgs?.size}")


        val infoList = mutableListOf<TeacherInfo>()

        //创建游标对象
        val cursor = readDatabase.query(
            "teacher_info",
            arrayOf(
                "id",
                "teacher_number",
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
            val teacherNumber = cursor.getString(cursor.getColumnIndex("teacher_number").minus(0))
            val name = cursor.getString(cursor.getColumnIndex("name").minus(0))
            val gender = cursor.getInt(cursor.getColumnIndex("gender").minus(0))
            val phone = cursor.getString(cursor.getColumnIndex("phone").minus(0))
            val birthday = cursor.getString(cursor.getColumnIndex("birthday").minus(0))
            val address = cursor.getString(cursor.getColumnIndex("address").minus(0))

            val teacherInfo = TeacherInfo(id, teacherNumber, name, gender, phone, birthday, address)
            infoList.add(teacherInfo)
        }
        // 关闭游标，释放资源
        cursor.close()
        return infoList
    }


    fun delete(teacherInfo: TeacherInfo) {
        //根据id删除
        val delete = writeDatabase.delete("teacher_info", "id=?", arrayOf(teacherInfo.id.toString()))
        Log.d(TAG, "delete: $delete")
    }

    fun update(teacherInfo: TeacherInfo) {
        //根据id修改描述
        val values = ContentValues()
        values.put("teacher_number", teacherInfo.teacherNumber)
        values.put("name", teacherInfo.name)
        values.put("gender", teacherInfo.gender)
        values.put("phone", teacherInfo.phone)
        values.put("birthday", teacherInfo.birthday)
        values.put("address", teacherInfo.address)
        val update = writeDatabase.update("teacher_info", values, "id=?", arrayOf(teacherInfo.id.toString()))
        Log.d(TAG, "update: $update")
    }
}