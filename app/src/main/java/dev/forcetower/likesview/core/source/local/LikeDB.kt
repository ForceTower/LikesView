package dev.forcetower.likesview.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramMediaPage
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.source.local.dao.MediaDao
import dev.forcetower.likesview.core.source.local.dao.MediaPageDao
import dev.forcetower.likesview.core.source.local.dao.ProfileDao

@Database(
    entities = [
        InstagramProfile::class,
        InstagramMedia::class,
        InstagramMediaPage::class
    ],
    version = 4
)
@TypeConverters(
    value = [
        Converters::class
    ]
)
abstract class LikeDB : RoomDatabase() {
    abstract fun profile(): ProfileDao
    abstract fun media(): MediaDao
    abstract fun mediaPage(): MediaPageDao
}
