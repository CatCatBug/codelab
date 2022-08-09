package com.pisto.sqlite.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.text.TextUtils
import android.util.Log
import com.pisto.sqlite.entity.ClassInfo

class ClassInfoDao(
    private val readDatabase: SQLiteDatabase,
    private val writeDatabase: SQLiteDatabase
) {

    companion object {
        private const val TAG = "ClassInfoDao"
    }

    fun insert(classInfo: ClassInfo) {
        //创建存放数据的ContentValues对象
        val values = ContentValues()
        values.put("name", classInfo.name)
        //数据库执行插入命令
        //插入成功就返回记录的id否则返回-1
        val insert = writeDatabase.insert("class_info", null, values)
        Log.d(TAG, "insert: $insert")
    }


    fun queryAll(
        name: String?
    ) : List<ClassInfo> {
        //生成过滤语句
        val selectionBuild = StringBuilder()
        val selectionArgs = mutableListOf<String>()

        //判断是否需要模糊查询
        //模糊查询有三种方式 此处是第一种，第二种在下面，第三种就是硬编码写SQL语句
        if (!TextUtils.isEmpty(name)) {
            selectionBuild.append("name LIKE ?")
            selectionArgs.add("%" + name!! + "%")
        }


        var selectionStr = selectionBuild.toString()

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
    ): List<ClassInfo> {
        Log.d(TAG, "queryAll: selection = $selection   size = ${selectionArgs?.size}")

        val infoList = mutableListOf<ClassInfo>()
        //创建游标对象
        val cursor = readDatabase.query(
            "class_info",
            arrayOf(
                "id",
                "name"
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
            val name = cursor.getString(cursor.getColumnIndex("name").minus(0))

            val classInfo = ClassInfo(id, name)
            infoList.add(classInfo)
        }
        // 关闭游标，释放资源
        cursor.close()
        return infoList
    }


    fun delete(classInfo: ClassInfo) {
        //根据id删除
        val delete = writeDatabase.delete("class_info", "id=?", arrayOf(classInfo.id.toString()))
        Log.d(TAG, "delete: $delete")
    }

    fun update(classInfo: ClassInfo) {
        //根据id修改描述
        val values = ContentValues()
        values.put("name", classInfo.name)
        val update = writeDatabase.update("class_info", values, "id=?", arrayOf(classInfo.id.toString()))
        Log.d(TAG, "update: $update")
    }

}