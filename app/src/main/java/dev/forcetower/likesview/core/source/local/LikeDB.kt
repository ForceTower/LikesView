package dev.forcetower.likesview.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.source.local.dao.ProfileDao

@Database(entities = [
    InstagramProfile::class,
    InstagramMedia::class
], version = 1)
@TypeConverters(value = [
    Converters::class
])
abstract class LikeDB : RoomDatabase() {
    abstract fun profile(): ProfileDao
}