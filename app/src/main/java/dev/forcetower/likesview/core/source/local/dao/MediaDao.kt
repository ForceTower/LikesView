package dev.forcetower.likesview.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class MediaDao : BaseDao<InstagramMedia>() {
    @Query("SELECT * FROM InstagramMedia WHERE profileId = :userId ORDER BY takenAt DESC")
    abstract fun getMedias(userId: Long): PagingSource<Int, InstagramMedia>

    @Query("DELETE FROM InstagramMedia")
    abstract suspend fun deleteAll()
}
