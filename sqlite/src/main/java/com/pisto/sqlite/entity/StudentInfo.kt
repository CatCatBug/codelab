package com.pisto.sqlite.entity

data class StudentInfo(
    var id: Int,
    var studentNumber: String,
    var name: String,
    var gender: Int,
    var phone: String,
    var birthday: String,
    var address: String
)