package dev.forcetower.likesview.core.source.repository

import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.model.dto.InstagramProfilePartial
import dev.forcetower.likesview.core.model.dto.InstagramUserSearch
import dev.forcetower.likesview.core.source.local.LikeDB
import dev.forcetower.likesview.core.source.paging.InstagramMediaRemoteMediator
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
        return Pager(
            config = PagingConfig(12),
            pagingSourceFactory = { database.media().getMedias(userId) },
            remoteMediator = InstagramMediaRemoteMediator(userId, database, service)
        ).flow
    }

    fun profile(userId: Long) = liveData {
        emitSource(database.profile().getById(userId))
        try {
            val current = database.profile().getByIdDirect(userId)
            if (current != null) {
                val response = service.getUser(current.username)
                val profile = InstagramProfilePartial.createFromFetch(response)
                if (profile != null) {
                    database.profile().update(profile)
                } else {
                    Timber.d("Received profile is null")
                }
            } else {
                Timber.e("current user $userId is null")
            }
        } catch (error: Throwable) {
            Timber.e(error, "Failed profile fetch")
        }
    }

    suspend fun markProfileSelected(profile: InstagramProfile) {
        database.profile().markSelected(profile.username)
    }

    suspend fun onDeleteProfile(profile: InstagramProfile): Boolean {
        return database.withTransaction {
            val all = database.profile().getAllDirect().filter { it.id != profile.id }
            var next = database.profile().getCurrentDirect()

            if (next?.id == profile.id) {
                next = all.firstOrNull()
            }

            database.profile().delete(profile)

            if (next != null) { database.profile().markSelected(next.username) }
            next != null
        }
    }

    fun profilesCount() = database.profile().getCount()
}
