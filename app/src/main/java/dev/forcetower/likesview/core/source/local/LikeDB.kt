package dev.forcetower.likesview.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile

@Database(entities = [
    InstagramProfile::class,
    InstagramMedia::class
], version = 1)
@TypeConverters(value = [
    InstagramMediaContentsConverter::class
])
abstract class LikeDB : RoomDatabase() {
}