package srawat.eng.mishi_challenge.ui

import android.app.Application
import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import srawat.eng.mishi_challenge.data.Item
import srawat.eng.mishi_challenge.domain.ItemsRepository

class HomeViewModel(private val context: Application, private val repository: ItemsRepository) :
    AndroidViewModel(context) {

    private val _items = MutableLiveData<ItemsUiModel>()
    val items: LiveData<ItemsUiModel>
        get() = _items

    init {
//        _items.value = ArrayList<Item>()
        _items.value = ItemsUiModel(emptyList(), "")
    }

    private var itemId = 1

    private val cameraProviderLiveData = MutableLiveData<ProcessCameraProvider>()

    fun getProcessCameraProvider(): LiveData<ProcessCameraProvider> {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            cameraProviderLiveData.value = cameraProviderFuture.get()
        }, ContextCompat.getMainExecutor(context))

        return cameraProviderLiveData
    }

    fun getItems() = viewModelScope.launch(Dispatchers.Default) {
        val items = repository.fetchAllItems()
        Log.d("Shivam", "I got items " + items.size)
    }

    fun getItemByTitle(title: String) = viewModelScope.launch(Dispatchers.Default) {
        val item = repository.fetchItem(title)
        Log.d("Shivam", "I got the item")
        updateLiveData(item)
    }

    private fun updateLiveData(item: Item) {
        viewModelScope.launch(Dispatchers.Main) {
//            val oldItems = _items.value?.toMutableList()
//            oldItems?.add(item)
//            _items.postValue(oldItems)
            val oldState = _items.value
            val oldItems = oldState?.items?.toMutableList()
            oldItems?.add(item)
            val updatedUiModel = ItemsUiModel(oldItems!!, "")
            _items.postValue(updatedUiModel)
        }
    }

    fun addItemById() = viewModelScope.launch(Dispatchers.Default) {
        if(itemId > 4) {
            updateUiWithError("Sorry, more items cannot be added at the moment.")
        } else {
            val item = repository.fetchItemById(itemId++)
            if(item == null) {
                if(itemId > 1)
                    itemId--
                Log.d("Shivam","Retrying fetch " + itemId)
                retryFetchingItem()
            } else {
                updateLiveData(item)
            }
        }
    }

    fun retryFetchingItem() = viewModelScope.launch(Dispatchers.Default) {
        val item = repository.fetchItemById(itemId++)
        if(item == null) {
            updateUiWithError("Something went wrong. Please try again")
        } else {
            updateLiveData(item)
        }
    }

    fun removeItem(item: Item) {
//        val oldItems = _items.value?.toMutableList()
//        oldItems?.remove(item)
//        _items.postValue(oldItems)
        val oldState = _items.value
        val oldItems = oldState?.items?.toMutableList()
        oldItems?.remove(item)
        val updatedUiModel = ItemsUiModel(oldItems!!, "")
        _items.postValue(updatedUiModel)
        itemId--
    }

    fun updateUiWithError(error: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val oldState = _items?.value
            val uiModel = ItemsUiModel(oldState?.items!!, error)
            _items.postValue(uiModel)
        }
    }

    fun reemitItems() {

    }
}

data class ItemsUiModel(
    val items: List<Item>,
    val error: String
)