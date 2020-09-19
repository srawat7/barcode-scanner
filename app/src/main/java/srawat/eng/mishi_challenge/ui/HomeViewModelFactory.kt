package srawat.eng.mishi_challenge.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import srawat.eng.mishi_challenge.domain.ItemsRepository
import java.lang.IllegalArgumentException
import javax.inject.Inject

class HomeViewModelFactory @Inject constructor(val context: Application, val itemsRepository: ItemsRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass != HomeViewModel::class.java) {
            throw IllegalArgumentException("Unknown View Model class")
        }

        return HomeViewModel(context, itemsRepository) as T
    }
}