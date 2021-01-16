package dev.forcetower.likesview.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.forcetower.likesview.core.source.local.LikeDB
import dev.forcetower.likesview.core.source.local.migrations.Migrations
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LikeDB {
        return Room.databaseBuilder(context, LikeDB::class.java, "likes_test.db")
            .addMigrations(Migrations.M2TO3, Migrations.M3TO4)
            .build()
    }
}
