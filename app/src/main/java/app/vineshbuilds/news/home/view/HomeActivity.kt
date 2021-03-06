package app.vineshbuilds.news.home.view

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import app.vineshbuilds.news.R
import app.vineshbuilds.news.home.viewmodel.ArticleVm
import app.vineshbuilds.news.home.viewmodel.HomeViewModel
import app.vineshbuilds.news.home.viewmodel.HomeViewModel.ViewState.*
import app.vineshbuilds.news.home.viewmodel.HomeViewModel.ViewState.Success.FromNetwork
import app.vineshbuilds.news.util.GenericListAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.item_article.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private val vm: HomeViewModel by viewModel()
    private val filterSheet = FilterBottomSheet.getInstance(onClose = { vm.refreshNews() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val adapter = GenericListAdapter(viewProvider(), viewBinder())
        rvNewsList.adapter = adapter
        rvNewsList.layoutManager = LinearLayoutManager(this)
        srlNewsContainer.setOnRefreshListener { vm.refreshNews() }
        observeAndUpdateNews(adapter)
        filterSheet.setFilters(vm.filters)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_filter -> {
            filterSheet.show(supportFragmentManager)
            true
        }
        else -> super.onOptionsItemSelected(item)
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
                view.tvAgency.text = item.agency
                view.tvDate.text = item.publishedDate.toString()
                view.setOnClickListener { openChromeTab(item) }
            }
        }
    }

    private fun observeAndUpdateNews(adapter: GenericListAdapter<ViewModel>) {
        vm.refreshNews().observe(this, Observer {
            when (it) {
                is Loading -> srlNewsContainer.isRefreshing = true
                is Error -> showSnackBar("Error : ${it.throwable.message}")
                is Empty -> showSnackBar("Empty 🧐")
                is Success -> {
                    vm.addNewAgenciesIfAny(it.getNewsAgencies())
                    adapter.submitItems(vm.filterIfNecessary(it.articles))
                    if (it is FromNetwork) showSnackBar("News served Hot 🤩")
                }
            }
            srlNewsContainer.isRefreshing = false
        })
    }

    private fun openChromeTab(article: ArticleVm) = CustomTabsIntent.Builder()
        .build()
        .launchUrl(this, Uri.parse(article.urlToStory))

    private fun showSnackBar(text: String) = Snackbar.make(rvNewsList, text, Snackbar.LENGTH_LONG).show()
}
