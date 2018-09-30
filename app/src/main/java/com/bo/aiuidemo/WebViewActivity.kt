package com.bo.aiuidemo

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.toolbar_layout.*

class WebViewActivity : AppCompatActivity() {
    companion object {
        val WEBVIEW_URL = "webview_url"
    }

    private var mUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        mUrl = savedInstanceState?.getString(WEBVIEW_URL)!!
        webView_act.loadUrl(mUrl)
        initWebView()
        initEvent()
    }

    private fun initEvent() {
        llBack.visibility = View.VISIBLE
        llBack.setOnClickListener {
            if (webView_act.canGoBack()) {
                webView_act.goBack()
            } else {
                finish()
            }
        }
        tv_finish_work.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        webView_act.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView_act.onResume()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView_act.canGoBack()) {
            webView_act.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webSettings = webView_act.settings
        //支持缩放，默认为true。
        webSettings.setSupportZoom(false)
        //调整图片至适合webview的大小
        webSettings.useWideViewPort = true
        // 缩放至屏幕的大小
        webSettings.loadWithOverviewMode = true
        //设置默认编码
        webSettings.defaultTextEncodingName = "utf-8"
        //设置自动加载图片
        webSettings.loadsImagesAutomatically = true
        //允许访问文件
        webSettings.allowFileAccess = true
        //开启javascript
        webSettings.javaScriptEnabled = true
        //水平不显示
        webView_act.isHorizontalScrollBarEnabled = false
        //垂直不显示
        webView_act.isVerticalScrollBarEnabled = false

        webView_act.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (view?.canGoBack()!!) {
                    tv_finish_work.visibility = View.VISIBLE
                } else {
                    tv_finish_work.visibility = View.GONE
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (view?.canGoBack()!!) {
                    tv_finish_work.visibility = View.VISIBLE
                } else {
                    tv_finish_work.visibility = View.GONE
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                tvTitleName.text = view?.title
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }

        webView_act.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                pb_web_act.progress = newProgress
            }
        }
    }

    override fun onDestroy() {
        webView_act?.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
        webView_act?.clearHistory()
        webView_act?.parent
        super.onDestroy()
    }
}
