package cc.fastcv.contact

import kotlin.coroutines.*

var varFun1 = fun (name: String, age: Int):Float {
    return 88f
}

//testUpFun1 接收的参数为函数类型：(String, Int)->String
fun testUpFun1(getScore : (String, Int)->Float) {
    var score = getScore("fish", 18)
    println("student score:$score")
}

fun main(args: Array<String>) {
    //通过函数引用调用
    testUpFun1(varFun1)
}