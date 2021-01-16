package dev.forcetower.likesview.view.medias

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.source.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class MediasViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    private var mediaUserId: Long? = null
    private var profileUserId: Long? = null

    private var media: Flow<PagingData<InstagramMedia>>? = null
    private var profile: LiveData<InstagramProfile?>? = null

    @ExperimentalPagingApi
    fun medias(userId: Long, initialHint: Int? = null): Flow<PagingData<InstagramMedia>> {
        val currentMedia = media
        if (this.mediaUserId == userId && currentMedia != null) return currentMedia
        this.mediaUserId = userId
        val newMedia = repository.medias(userId, initialHint).cachedIn(viewModelScope)
        this.media = newMedia
        return newMedia
    }

    fun profile(userId: Long): LiveData<InstagramProfile?> {
        val currentProfile = profile
        if (this.profileUserId == userId && currentProfile != null) return currentProfile
        this.profileUserId = userId
        val newProfile = repository.profile(userId)
        this.profile = newProfile
        return newProfile
    }
}