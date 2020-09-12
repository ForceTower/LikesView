package dev.forcetower.likesview

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.forcetower.likesview.utils.crashlytics.CrashlyticsTree
import timber.log.Timber

@HiltAndroidApp
class LikeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // "Every time you log in production, a puppy dies"
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }
    }
}
