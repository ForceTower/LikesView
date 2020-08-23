package dev.forcetower.likesview.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.likesview.core.model.database.InstagramMediaPage
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class MediaPageDao : BaseDao<InstagramMediaPage>() {
    @Query("SELECT * FROM InstagramMediaPage WHERE id = :mediaId")
    abstract suspend fun getPageForMedia(mediaId: Int): InstagramMediaPage?

    @Query("DELETE FROM InstagramMediaPage")
    abstract suspend fun deleteAll()
}