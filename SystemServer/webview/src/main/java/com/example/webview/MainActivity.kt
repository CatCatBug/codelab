package com.example.webview

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.webkit.*

class MainActivity : AppCompatActivity() {

    private val url = "http://192.168.3.109:8080/index.html"

    private lateinit var webView:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)

        initWebview()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebview() {
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //使用WebView加载显示url
                view.loadUrl(url)
                //返回true
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                //调用H5页面javascript方法setMCUserToken
                webView.loadUrl("javascript:(window.setMCUserToken('jsonStr'))")
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    Log.d("webview", it.message() + " -- From line " + it.lineNumber() + " of " + it.sourceId());
                }
                return super.onConsoleMessage(consoleMessage)
            }
        };

        //支持js
        webView.settings.javaScriptEnabled = true
        // 解决图片不显示
        webView.settings.blockNetworkImage = false
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        //自适应屏幕
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.settings.loadWithOverviewMode = true
        //设置可以支持缩放
        webView.settings.setSupportZoom(false)
        //扩大比例的缩放
        webView.settings.useWideViewPort = false
        //设置是否出现缩放工具
        webView.settings.builtInZoomControls = false

        webView.addJavascriptInterface(MyJSI(this), "PosObj")

        //访问网页
        webView.loadUrl(url)
    }

    override fun onDestroy() {
        val parent = webView.parent
        if (parent != null) {
            (parent as ViewGroup).removeView(webView)
        }
        webView.removeAllViews()
        webView.destroy()
        super.onDestroy()
    }
}