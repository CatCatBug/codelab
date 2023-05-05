package cc.fastcv.sqlite.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase

object Database {

    private lateinit var readDatabase : SQLiteDatabase
    private lateinit var writeDatabase : SQLiteDatabase

    private var studentInfoDao: StudentInfoDao? = null

    fun init(context: Context) {
        readDatabase = DatabaseHelper(context).readableDatabase
        writeDatabase = DatabaseHelper(context).writableDatabase
    }


    fun getStudentInfoDao() : StudentInfoDao {
        if (studentInfoDao == null) {
            studentInfoDao = StudentInfoDao(readDatabase, writeDatabase)
        }
        return studentInfoDao!!
    }

}