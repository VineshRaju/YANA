package app.vineshbuilds.news.home.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import app.vineshbuilds.news.R
import app.vineshbuilds.news.home.viewmodel.ArticleState
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import app.vineshbuilds.news.util.GenericListAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_article.view.*

class HomeActivity : AppCompatActivity() {
    private val adapter: GenericListAdapter<ViewModel> by lazy {
        GenericListAdapter(
            this, viewProvider(), viewBinder()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val vm = ViewModelProviders.of(this@HomeActivity).get(HomeViewModel::class.java)
        rvArticles.layoutManager = LinearLayoutManager(this)
        rvArticles.adapter = adapter
        vm.getArticles().observe(this, Observer {
            when (it) {
                is ArticleState.Error -> showSnackBar("Error : ${it.throwable.message}")
                is ArticleState.Empty -> showSnackBar("Empty :o")
                is ArticleState.ArticlesFromCache -> adapter.submitItems(it.articles).also {
                    showSnackBar("Showing from Cache")
                }
                is ArticleState.ArticlesFromNetwork -> adapter.submitItems(it.articles).also {
                    showSnackBar("News served Hot XD")
                }
            }
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
                Picasso.get().load(item.thumbUrl).fit().centerCrop().into(view.ivThumb)
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
        Snackbar.make(rvArticles, text, Snackbar.LENGTH_LONG).show()
    }
}
