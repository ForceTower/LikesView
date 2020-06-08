package dev.forcetower.likesview.dagger.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dev.forcetower.likesview.dagger.module.activity.MainActivityModule
import dev.forcetower.likesview.view.LauncherActivity
import dev.forcetower.likesview.view.MainActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun launcher(): LauncherActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun main(): MainActivity
}