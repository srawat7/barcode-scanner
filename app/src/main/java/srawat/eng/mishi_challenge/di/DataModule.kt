package srawat.eng.mishi_challenge.di

import dagger.Module
import dagger.Provides
import srawat.eng.core.FeatureScope
import srawat.eng.mishi_challenge.data.AppDatabase
import srawat.eng.mishi_challenge.ui.HomeActivity


@Module
class DataModule(val activity: HomeActivity) {

//    @Provides
//    @FeatureScope
//    fun provideItemsService(
//        client: OkHttpClient,
//        gson: Gson
//    ): ItemsService {
//        return Retrofit.Builder()
//            .baseUrl("")
//            .callFactory(client)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//            .create(ItemsService::class.java)
//    }

    @Provides
    @FeatureScope
    fun provideDatabase() = AppDatabase.get(activity).itemsDao()

}