package app.vineshbuilds.news.util


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.vineshbuilds.news.util.GenericListAdapter.GenericViewHolder

class GenericListAdapter<T : ViewModel>(
    @LayoutRes private val viewProvider: (item: T) -> Int,
    private val binder: (view: View, item: T) -> Unit
) : Adapter<GenericViewHolder<T>>() {
    private val diffHelper = AsyncListDiffer(this, ViewModelDiffCallback<T>())

    fun submitItems(items: List<T>) {
        diffHelper.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): GenericViewHolder<T> {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewProvider.invoke(getItemAt(pos)), parent, false)
        return GenericViewHolder(view, binder)
    }

    override fun getItemCount() = getItems().size

    override fun onBindViewHolder(holder: GenericViewHolder<T>, pos: Int) {
        holder.bind(getItemAt(pos))
    }

    private fun getItemAt(pos: Int) = getItems()[pos]

    private fun getItems(): List<T> = diffHelper.currentList

    class GenericViewHolder<T : ViewModel>(itemView: View, private val binder: (view: View, item: T) -> Unit) :
        ViewHolder(itemView) {
        fun bind(item: T) {
            binder.invoke(itemView, item)
        }
    }
}
