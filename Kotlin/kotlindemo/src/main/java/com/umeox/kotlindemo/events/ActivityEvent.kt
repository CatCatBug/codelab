package com.umeox.kotlindemo.events

class ActivityEvent {

    var arg1:String? = null
    var arg2:String? = null
    override fun toString(): String {
        return "ActivityEvent(arg1=$arg1, arg2=$arg2)"
    }

}