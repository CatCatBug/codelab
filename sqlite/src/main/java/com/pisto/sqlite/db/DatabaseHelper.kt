package com.pisto.sqlite.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * 数据库帮助类
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION) {
    companion object {
        const val DB_NAME = "pisto.db"
        const val DB_VERSION = 1
        const val TAG = "DatabaseHelper"
    }

    /**
     * 每个版本初始化的时候会调用  之后就不再调用此方法了，所以就可以在这个方法里面做以下操作：
     * 1、创建表
     *
     */
    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(TAG, "创建数据库 当前版本 ${db?.version}  数据库存储路径 ${db?.path}")
        //版本1创建的表
        db?.execSQL(CREATE_STUDENT_INFO_TABLE)
        db?.execSQL(CREATE_CLASS_INFO_TABLE)
        db?.execSQL(CREATE_TEACHER_INFO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: 数据库升级--------- oldVersion = $oldVersion  newVersion = $newVersion ")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: 数据库降级-------- oldVersion = $oldVersion  newVersion = $newVersion ")
    }
}