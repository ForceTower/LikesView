package dev.forcetower.likesview.dagger.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.forcetower.likesview.core.source.remote.InstagramAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideService(client: OkHttpClient): InstagramAPI {
        return Retrofit.Builder()
            .baseUrl("https://www.instagram.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InstagramAPI::class.java)
    }
}