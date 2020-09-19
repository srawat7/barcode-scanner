package srawat.eng.mishi_challenge.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import srawat.eng.mishi_challenge.ui.HomeViewModel
import srawat.eng.mishi_challenge.data.AppDatabase
import srawat.eng.mishi_challenge.data.ItemsLocalDataSource
import srawat.eng.mishi_challenge.data.ItemsRepositoryImpl
import srawat.eng.mishi_challenge.domain.ItemsRepository
import srawat.eng.mishi_challenge.ui.HomeActivity
import srawat.eng.mishi_challenge.ui.HomeViewModelFactory

@Module
class HomeModule(private val activity: HomeActivity) {

    @Provides
    fun provideHomeViewModel(
        factory: HomeViewModelFactory
    ): HomeViewModel =
        ViewModelProvider(activity, factory).get(HomeViewModel::class.java)

    @Provides
    fun provideHomeViewModelFactory(
        itemsRepository: ItemsRepository
    ): HomeViewModelFactory =
        HomeViewModelFactory(activity.application, itemsRepository)

    @Provides
    fun provideItemsRepository(): ItemsRepository =
        ItemsRepositoryImpl(ItemsLocalDataSource(AppDatabase.get(activity).itemsDao()))
}