package app.vineshbuilds.news.home.view

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import app.vineshbuilds.news.R
import app.vineshbuilds.news.detail.view.WebViewActivity
import app.vineshbuilds.news.home.view.model.NewsModel.ArticleModel
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import app.vineshbuilds.news.util.GenericListAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_article.view.*

class HomeActivity : AppCompatActivity() {
    private val viewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this@HomeActivity)
            .get(HomeViewModel::class.java)
    }

    private val adapter: GenericListAdapter<ArticleVm> by lazy {
        GenericListAdapter(
            this, viewModel.refreshNews(), { R.layout.item_article }, bind()
        )
    }

    private fun bind() = { view: View, item: ViewModel ->
        when (item) {
            is ArticleVm -> {
                Picasso.get().load(item.thumbUrl).fit().centerCrop().into(view.ivThumb)
                view.tvTitle.text = item.headline
                view.tvBody.text = item.story
                view.tvDate.text = item.publishedDate.toString()
                view.setOnClickListener { openChromeTab(item) }
            }
        }
    }


    private fun openWebView(article: ArticleModel) =
        WebViewActivity.createIntent(this, article.urlToStory)
            .let(this::startActivity)

    private fun openChromeTab(article: ArticleVm) =
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(this, Uri.parse(article.urlToStory))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        rvArticles.layoutManager = LinearLayoutManager(this)
        rvArticles.adapter = adapter
        //viewModel.cacheNews(it)
    }
}
