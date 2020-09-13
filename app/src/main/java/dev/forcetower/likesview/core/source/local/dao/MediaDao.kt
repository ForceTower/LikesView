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

    @Query("DELETE FROM InstagramMedia WHERE profileId = :userId")
    abstract suspend fun deleteAll(userId: Long)

    @Query("SELECT id FROM InstagramMedia WHERE profileId = :userId")
    abstract suspend fun getUserMediaIds(userId: Long): List<Int>
}
