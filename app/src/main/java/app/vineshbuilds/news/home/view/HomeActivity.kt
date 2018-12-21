package app.vineshbuilds.news.home.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import app.vineshbuilds.news.R
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import app.vineshbuilds.news.home.viewmodel.NewsState
import app.vineshbuilds.news.home.viewmodel.NewsState.*
import app.vineshbuilds.news.util.GenericListAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_article.view.*

class HomeActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProviders.of(this@HomeActivity).get(HomeViewModel::class.java)
    }

    private val adapter: GenericListAdapter<ViewModel> by lazy {
        GenericListAdapter(viewProvider(), viewBinder())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        rvNewsList.layoutManager = LinearLayoutManager(this)
        rvNewsList.adapter = adapter
        srlNewsContainer.setOnRefreshListener { homeViewModel.getNews() }
        observe(homeViewModel.getNews())
    }

    private fun observe(news: LiveData<NewsState>) {
        news.observe(this, Observer {
            when (it) {
                is Loading -> srlNewsContainer.isRefreshing = true
                is Error -> showSnackBar("Error : ${it.throwable.message}")
                is Empty -> showSnackBar("Empty 🧐")
                is ArticlesFromCache -> adapter.submitItems(it.articles)
                is ArticlesFromNetwork -> adapter.submitItems(it.articles).also {
                    showSnackBar("News served Hot 🤩")
                }
            }
            srlNewsContainer.isRefreshing = false
        })
    }

    private fun viewProvider() = { item: ViewModel ->
        when (item) {
            is ArticleVm -> R.layout.item_article
            else -> -1
        }
    }

    private fun viewBinder() = { view: View, item: ViewModel ->
        when (item) {
            is ArticleVm -> {
                Picasso.get().load(item.thumbUrl).noFade().fit().centerCrop().into(view.ivThumb)
                view.tvTitle.text = item.headline
                view.tvBody.text = item.story
                view.tvDate.text = item.publishedDate.toString()
                view.setOnClickListener { openChromeTab(item) }
            }
        }
    }

    private fun openChromeTab(article: ArticleVm) =
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(this, Uri.parse(article.urlToStory))

    private fun showSnackBar(text: String) {
        Snackbar.make(rvNewsList, text, Snackbar.LENGTH_LONG).show()
    }
}
