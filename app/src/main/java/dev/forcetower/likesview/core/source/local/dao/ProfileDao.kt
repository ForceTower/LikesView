package dev.forcetower.likesview.core.source.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.model.dto.InstagramProfilePartial
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProfileDao : BaseDao<InstagramProfile>() {
    @Query("SELECT * FROM InstagramProfile WHERE username = :username")
    abstract suspend fun getByUsernameDirect(username: String): InstagramProfile?

    @Query("SELECT * FROM InstagramProfile ORDER BY username")
    abstract fun getAll(): Flow<List<InstagramProfile>>

    @Query("SELECT * FROM InstagramProfile")
    abstract fun getAllDirect(): List<InstagramProfile>

    @Query("SELECT * FROM InstagramProfile WHERE id = :userId")
    abstract fun getById(userId: Long): LiveData<InstagramProfile?>

    @Query("SELECT * FROM InstagramProfile WHERE id = :userId")
    abstract suspend fun getByIdDirect(userId: Long): InstagramProfile?

    @Query("UPDATE InstagramProfile SET selected = username = :username")
    abstract suspend fun markSelected(username: String)

    @Query("SELECT * FROM InstagramProfile WHERE selected = 1")
    abstract fun getCurrent(): Flow<InstagramProfile?>

    @Update(entity = InstagramProfile::class, onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(partial: InstagramProfilePartial)

    override suspend fun getValueByIDDirect(value: InstagramProfile): InstagramProfile? {
        return getByUsernameDirect(value.username)
    }

    @Query("SELECT COUNT(*) FROM InstagramProfile")
    abstract fun getCount(): Flow<Int>
}
