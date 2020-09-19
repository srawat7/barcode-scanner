package srawat.eng.mishi_challenge.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_cart.*
import srawat.eng.mishi_challenge.R
import srawat.eng.mishi_challenge.data.Item

class CartFragment : Fragment(){

    private lateinit var itemsAdapter: ItemAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Shivam", "cart fragment")
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
//        viewModel.items.observe(requireActivity(), Observer<ItemsUiModel> {
////            if(it.isNotEmpty())
////                Log.d("Shivam", "The item uri is " + it[0].imageUrl)
//            if(itemsAdapter != null)
//                itemsAdapter.items = it.items
//        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        itemsAdapter = ItemAdapter(activity, object : ItemsAdapterListener {
            override fun onRemoveItem(item: Item) {
                viewModel.removeItem(item)
            }

            override fun refreshList() {
                Log.d("Shivam", "Reemitting Items")
                //
                 viewModel.reemitItems()
            }

        })

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        viewModel.items.observe(requireActivity(), Observer<ItemsUiModel> {
//            if(it.isNotEmpty())
//                Log.d("Shivam", "The item uri is " + it[0].imageUrl)
            if(itemsAdapter != null)
                itemsAdapter.items = it.items.toMutableList()
        })

//        val item1 = Item(12132313,"banana", 12.22, "file:///android_asset/nike_shoes.jpeg")
//        val item2 = Item(1238383, "apple",  20.50, "file:///android_asset/pixel_4a.jpg")
//
//        val l = listOf<Item>(item1, item2)
//        itemsAdapter.items = l

        items_recyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = itemsAdapter
        }
    }
}