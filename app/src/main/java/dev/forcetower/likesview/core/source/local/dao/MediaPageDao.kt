package dev.forcetower.likesview.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.likesview.core.model.database.InstagramMediaPage
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class MediaPageDao : BaseDao<InstagramMediaPage>() {
    @Query("SELECT * FROM InstagramMediaPage WHERE id = :mediaId")
    abstract suspend fun getPageForMedia(mediaId: Int): InstagramMediaPage?

    @Query("DELETE FROM InstagramMediaPage WHERE id IN (:ids)")
    abstract suspend fun deleteAll(ids: List<Int>)

    @Query("SELECT currentPageId FROM InstagramMediaPage WHERE nextMaxId = :page")
    abstract suspend fun getPreviousPage(page: String?): String?
}
