package com.pisto.sqlite.entity

data class TeacherInfo(
    var id: Int,
    var teacherNumber: String,
    var name: String,
    var gender: Int,
    var phone: String,
    var birthday: String,
    var address: String
)