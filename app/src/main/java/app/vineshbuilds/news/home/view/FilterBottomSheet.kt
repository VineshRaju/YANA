package app.vineshbuilds.news.home.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.vineshbuilds.news.R
import app.vineshbuilds.news.home.viewmodel.FilterVm
import app.vineshbuilds.news.util.GenericListAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_filters.view.*
import kotlinx.android.synthetic.main.item_filter.view.*

class FilterBottomSheet : BottomSheetDialogFragment() {
    private val adapter = GenericListAdapter({ R.layout.item_filter }, this::binder)
    private var onClose: () -> Unit = {}

    companion object {
        fun getInstance(onClose: () -> Unit): FilterBottomSheet = FilterBottomSheet().apply {
            this.onClose = onClose
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_filters, container, true)
        view.rvFilterList.adapter = adapter
        view.rvFilterList.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onClose.invoke()
    }

    private fun binder(view: View, item: FilterVm) {
        view.apply {
            cbIsApplied.text = item.filterName
            cbIsApplied.isChecked = item.isSelected
            cbIsApplied.setOnCheckedChangeListener { _, isChecked ->
                item.isSelected = isChecked
            }
        }
    }

    fun setFilters(newsAgencies: List<FilterVm>) {
        adapter.submitItems(newsAgencies)
    }

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, "FILTER_POPUP")
}
