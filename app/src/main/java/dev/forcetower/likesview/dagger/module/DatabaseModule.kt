package dev.forcetower.likesview.dagger.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.forcetower.likesview.core.source.local.LikeDB
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): LikeDB {
        return Room.databaseBuilder(context, LikeDB::class.java, "likes.db").build()
    }
}