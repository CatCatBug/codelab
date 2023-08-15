package com.example.webview

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface

class MyJSI(private val context: Context) {
    @JavascriptInterface
    fun invokeFun(funName:String,json:String?) {
        Log.d("MyJSI", "invokeFun: funName = $funName  json = $json ")
    }

    //通用
    @JavascriptInterface
    fun close() {
        //js调用完以后的逻辑
    }
}