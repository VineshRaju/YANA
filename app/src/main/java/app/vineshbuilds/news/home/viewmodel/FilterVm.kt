package app.vineshbuilds.news.home.viewmodel

import androidx.lifecycle.ViewModel

data class FilterVm(val filterName: String, var isSelected: Boolean = true) : ViewModel()
