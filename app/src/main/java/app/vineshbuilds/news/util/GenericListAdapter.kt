package app.vineshbuilds.news.util


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.vineshbuilds.news.util.GenericListAdapter.GenericViewHolder

class GenericListAdapter<T : ViewModel>(
    lifecycleOwner: LifecycleOwner,
    liveItems: LiveData<List<T>?>,
    @LayoutRes private val viewProvider: (item: T) -> Int,
    private val binder: (view: View, item: T) -> Unit
) : Adapter<GenericViewHolder<T>>() {

    val items = mutableListOf<T>()

    init {
        liveItems.observe(lifecycleOwner, Observer {
            it?.let { newItems ->
                items.clear()
                items.addAll(newItems)
                notifyDataSetChanged()
                //calculateDiff(newItems).dispatchUpdatesTo(this)
            }
        })
    }

    /* private fun calculateDiff(newItems: List<T>) = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
         override fun areItemsTheSame(p0: Int, p1: Int): Boolean = items[p0] === newItems[p1]

         override fun getOldListSize() = items.size

         override fun getNewListSize() = newItems.size

         override fun areContentsTheSame(p0: Int, p1: Int) = false
     })*/


    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): GenericViewHolder<T> {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewProvider.invoke(getItemAt(pos)), parent, false)
        return GenericViewHolder(view, binder)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: GenericViewHolder<T>, pos: Int) {
        holder.bind(getItemAt(pos))
    }

    private fun getItemAt(pos: Int) = items[pos]

    class GenericViewHolder<T : ViewModel>(itemView: View, private val binder: (view: View, item: T) -> Unit) :
        ViewHolder(itemView) {
        fun bind(item: T) {
            binder.invoke(itemView, item)
        }
    }
}
