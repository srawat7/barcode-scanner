package srawat.eng.mishi_challenge.ui

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_item.view.*
import srawat.eng.mishi_challenge.R
import srawat.eng.mishi_challenge.data.Item
import java.text.NumberFormat
import java.util.*

class ItemAdapter(val activity: Activity?, val listener: ItemsAdapterListener) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    var items: List<Item> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false),
            activity,
            listener
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("Shivam", "trying to bind position $position")
        Log.d("Shivam", "size of items is " + items.size)
        val item = items[position]
        if (item == null) {
            Log.d("Shivam", "Item was null in adapter")
//            val mutableItems = items.toMutableList()
//            mutableItems.remove(item)
//            items = mutableItems
        } else {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size

    class ItemViewHolder(
        val view: View?,
        val activity: Activity?,
        val listener: ItemsAdapterListener?
    ) :
        RecyclerView.ViewHolder(view!!) {
        private val title: TextView? = view?.title
        private val price = view?.price
        private val image: ImageView? = view?.image
        private val removeButton: MaterialButton? = view?.remove_button

        fun bind(item: Item?) {
            if (view != null && item != null) {
                title?.text = item.title
                price?.text =
                    NumberFormat.getCurrencyInstance(Locale("en", "US")).format(item.price)
                if (activity != null)
                    Glide.with(activity)
                        .load(Uri.parse(item.imageUrl))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .fitCenter()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(image!!)

                removeButton?.setOnClickListener {
                    listener?.onRemoveItem(item)
                }
            } else {
                listener?.refreshList()
            }
        }
    }
}