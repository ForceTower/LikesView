package dev.forcetower.likesview.dagger.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import android.webkit.WebSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.forcetower.likesview.BuildConfig
import dev.forcetower.likesview.LikeApp
import dev.forcetower.likesview.R
import dev.forcetower.likesview.dagger.annotations.ShowInstrackerOfferFlag
import dev.forcetower.toolkit.network.MemoryCookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: LikeApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideClient(
        @Named("UserAgentInterceptor") interceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .cookieJar(MemoryCookieJar())
            .addInterceptor(interceptor)
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG)
                        HttpLoggingInterceptor.Level.BASIC
                    else
                        HttpLoggingInterceptor.Level.NONE
                }
            )
            .build()
    }

    @Provides
    @Singleton
    @Named("UserAgentInterceptor")
    fun provideInterceptor(
        @Named("WebViewUA") userAgent: String
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("User-Agent", userAgent)
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Cache-Control", "max-age=0")
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .create()
    }

    @Provides
    @Reusable
    @Named("WebViewUA")
    fun provideWebViewUserAgent(@ApplicationContext context: Context): String = WebSettings.getDefaultUserAgent(context)

    @Provides
    @Reusable
    fun provideRemoteConfig(): FirebaseRemoteConfig {
        val config = FirebaseRemoteConfig.getInstance()
        val settings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(900L)
            .setFetchTimeoutInSeconds(900L)
            .build()

        config.setConfigSettingsAsync(settings)
        config.setDefaultsAsync(R.xml.remote_config_defaults)
        config.fetchAndActivate().addOnCompleteListener {
            if (!it.isSuccessful) {
                Timber.d("Failed to init remote config")
            }
        }
        return config
    }

    @Provides
    @ShowInstrackerOfferFlag
    fun provideShowInstrackerFrag(config: FirebaseRemoteConfig): Boolean {
        return config.getBoolean("show_instracking_offer_flag")
    }

    @Provides
    @Reusable
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }
}
