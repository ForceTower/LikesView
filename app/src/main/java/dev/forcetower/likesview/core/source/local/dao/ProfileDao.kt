package dev.forcetower.likesview.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProfileDao : BaseDao<InstagramProfile>() {
    @Query("SELECT * FROM InstagramProfile WHERE username = :username")
    abstract suspend fun getByUsernameDirect(username: String): InstagramProfile?

    @Query("SELECT * FROM InstagramProfile")
    abstract fun getAll(): Flow<List<InstagramProfile>>

    @Query("UPDATE InstagramProfile SET selected = username = :username")
    abstract fun markSelected(username: String)

    @Query("SELECT * FROM InstagramProfile WHERE selected = 1")
    abstract fun getCurrent(): Flow<InstagramProfile?>
}