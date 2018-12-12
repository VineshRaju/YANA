package app.vineshbuilds.news.detail.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import app.vineshbuilds.news.R
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : AppCompatActivity() {
    companion object {
        fun createIntent(context: Context, urlToLoad: String) =
            Intent(context, WebViewActivity::class.java).apply {
                putExtra("URL", urlToLoad)
            }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        (intent.extras!!["URL"] as String).let { wvWebView.loadUrl(it) }
    }
}
