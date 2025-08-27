package cn.qfys521.edu_system

import android.annotation.SuppressLint
// import android.graphics.Color // 不再在此处显式设置颜色
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
// import androidx.core.view.WindowInsetsControllerCompat // 不再显式控制外观
import androidx.core.view.updatePadding

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 启用沉浸式边到边显示
        // 主题现在将控制状态栏和导航栏的颜色/外观
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_webview)

        val rootLayout = findViewById<View>(R.id.root_container)
        webView = findViewById(R.id.webView)

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            view.updatePadding(
                left = insets.left,
                top = insets.top,
                right = insets.right,
                bottom = insets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        // 配置 WebView 设置
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        // 配置 CookieManager
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)

        // 设置 WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
        }

        // 加载 URL
        val url = "https://authserver.open.ha.cn/authserver/login?service=https%3A%2F%2Fjwxt.haou.edu.cn%2Fapp-ws%2Fws%2Fapp-service%2Fjasig%2Fcas%2Findex"
        webView.loadUrl(url)

        // 处理返回按钮按下事件
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onPause() {
        super.onPause()
        CookieManager.getInstance().flush()
    }
}
