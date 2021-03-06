package srawat.eng.core

import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Component(modules = [CoreDataModule::class]) // :: is for getting a runtime reference of the class via reflection
@Singleton
interface CoreComponent {

    @Component.Builder interface Builder {
        fun build(): CoreComponent
    }

    fun provideOkHttpClient(): OkHttpClient
    fun provideGson(): Gson
    fun provideGsonConverterFactory(): GsonConverterFactory
}