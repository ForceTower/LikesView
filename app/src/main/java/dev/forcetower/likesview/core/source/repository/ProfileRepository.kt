package dev.forcetower.likesview.core.source.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.model.dto.InstagramUserSearch
import dev.forcetower.likesview.core.source.local.LikeDB
import dev.forcetower.likesview.core.source.paging.InstagramMediaSource
import dev.forcetower.likesview.core.source.remote.InstagramAPI
import dev.forcetower.toolkit.extensions.limit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val service: InstagramAPI,
    private val database: LikeDB
) {
    fun profiles() = database.profile().getAll()
    fun currentProfile() = database.profile().getCurrent()

    suspend fun search(username: String): List<InstagramUserSearch> = withContext(Dispatchers.IO) {
        service.topSearch(username).users?.limit(3)?.map { it.user } ?: emptyList()
    }

    suspend fun addProfile(profile: InstagramUserSearch) {
        database.withTransaction {
            val data = database.profile().getByUsernameDirect(profile.username)
            if (data == null) {
                database.profile().insert(InstagramProfile.createFromSearch(profile))
            }
            database.profile().markSelected(profile.username)
        }
    }

    fun medias(userId: Long): Flow<PagingData<InstagramMedia>> {
        return Pager(PagingConfig(12)) {
            InstagramMediaSource(userId, database, service)
        }.flow
    }

    fun profile(userId: Long): Flow<InstagramProfile?> {
        return database.profile().getById(userId)
    }

    suspend fun markProfileSelected(profile: InstagramProfile) {
        database.profile().markSelected(profile.username)
    }

    fun profilesCount() = database.profile().getCount()
}