package dev.forcetower.likesview.dagger

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.forcetower.likesview.dagger.module.ViewModelModule
import dev.forcetower.likesview.LikeApp
import dev.forcetower.likesview.dagger.module.ActivityModule
import dev.forcetower.likesview.dagger.module.AppModule
import dev.forcetower.likesview.dagger.module.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppModule::class,
    DatabaseModule::class,
    ActivityModule::class,
    ViewModelModule::class
])
interface AppComponent : AndroidInjector<LikeApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: LikeApp): Builder
        fun build(): AppComponent
    }
}