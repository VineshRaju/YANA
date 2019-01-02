package app.vineshbuilds.news.util

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import app.vineshbuilds.news.home.viewmodel.ArticleVm

class ViewModelDiffCallback<T : ViewModel> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = if (oldItem is ArticleVm && newItem is ArticleVm) {
        oldItem.urlToStory == newItem.urlToStory
    } else false

    override fun areContentsTheSame(oldItem: T, newItem: T) = if (oldItem is ArticleVm && newItem is ArticleVm) {
        oldItem.headline == newItem.headline
    } else false
}
