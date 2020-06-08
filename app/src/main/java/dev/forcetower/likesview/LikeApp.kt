package dev.forcetower.likesview

import dagger.android.support.DaggerApplication
import dev.forcetower.likesview.dagger.AppComponent
import dev.forcetower.likesview.dagger.DaggerAppComponent
import timber.log.Timber

class LikeApp : DaggerApplication() {
    private val component: AppComponent by lazy { createComponent() }

    override fun onCreate() {
        super.onCreate()
        // "Every time you log in production, a puppy dies"
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun createComponent(): AppComponent {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun applicationInjector() = component
}