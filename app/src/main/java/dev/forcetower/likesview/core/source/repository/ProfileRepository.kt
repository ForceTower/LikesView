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
import dev.forcetower.toolkit.utils.string.StringSimilarity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
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
        try {
            service.topSearch(username).users?.sortedByDescending {
                StringSimilarity.similarity(username, it.user.username)
            }?.limit(3)?.map { it.user } ?: emptyList()
        } catch (error: Throwable) {
            Timber.d(error, "Top search request failed")
            emptyList<InstagramUserSearch>()
        }
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

    suspend fun onDeleteProfile(profile: InstagramProfile): Boolean {
        return database.withTransaction {
            val all = database.profile().getAllDirect().filter { it.id != profile.id }
            database.profile().delete(profile)
            val first = all.firstOrNull()
            if (first != null) { database.profile().markSelected(first.username) }
            first != null
        }
    }

    fun profilesCount() = database.profile().getCount()
}