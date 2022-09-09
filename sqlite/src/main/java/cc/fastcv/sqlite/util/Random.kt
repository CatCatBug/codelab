package cc.fastcv.sqlite.util

import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.*
import kotlin.math.abs

fun randomName(): String {
    val list = 2..5
    val length = list.random()
    var name = ""
    for (i in 1..length) {
        name += getRandomChar()
    }

    return name
}

fun randomAddress(): String {
    val list = 10..25
    val length = list.random()
    var address = ""
    for (i in 1..length) {
        address += getRandomChar()
    }

    return address
}

fun randomPhoneNumber(): String {
    val list = 0..9
    var phoneNumber = "1"
    for (i in 1..10) {
        phoneNumber += list.random()
    }
    return phoneNumber
}

fun randomGender(): Int {
    val list = 0..1
    return list.random()
}

fun randomYear() : Int {
    val list = 0..29
    return list.random()
}

fun randomMonth() : Int {
    val list = 0..11
    return list.random()
}


fun randomDay() : Int {
    val list = 0..30
    return list.random()
}


//随机生成汉字
private fun getRandomChar(): Char {
    var str = ""
    val heightPos: Int //
    val lowPos: Int
    val random = Random()
    heightPos = 176 + abs(random.nextInt(39))
    lowPos = 161 + abs(random.nextInt(93))
    val b = ByteArray(2)
    b[0] = Integer.valueOf(heightPos).toByte()
    b[1] = Integer.valueOf(lowPos).toByte()
    try {
        str = String(b, Charset.forName("GBK"))
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
        println("错误")
    }
    return str[0]
}
