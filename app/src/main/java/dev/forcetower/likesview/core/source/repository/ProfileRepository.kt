package dev.forcetower.likesview.core.source.repository

import androidx.room.withTransaction
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.model.dto.InstagramUserSearch
import dev.forcetower.likesview.core.source.local.LikeDB
import dev.forcetower.likesview.core.source.remote.InstagramAPI
import dev.forcetower.toolkit.extensions.limit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val service: InstagramAPI,
    private val database: LikeDB
) {
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

    fun profiles() = database.profile().getAll()
    fun currentProfile() = database.profile().getCurrent()
}