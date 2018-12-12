package app.vineshbuilds.news.home.repository

import android.arch.lifecycle.LiveData
import app.vineshbuilds.news.home.viewmodel.ArticleVm

interface Source {
     fun refreshNews(): LiveData<List<ArticleVm>?>
}
