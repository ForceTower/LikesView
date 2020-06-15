package dev.forcetower.likesview.view.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.source.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class ProfileViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
) : ViewModel() {
    private var mediaUserId: Long? = null
    private var profileUserId: Long? = null

    private lateinit var media: Flow<PagingData<InstagramMedia>>
    private lateinit var profile: LiveData<InstagramProfile?>

    fun media(userId: Long): Flow<PagingData<InstagramMedia>> {
        if (this.mediaUserId == userId) return media
        this.mediaUserId = userId
        media = repository.medias(userId).cachedIn(viewModelScope)
        return media
    }

    fun profile(userId: Long): LiveData<InstagramProfile?> {
        if (this.profileUserId == userId) return profile
        this.profileUserId = userId
        profile = repository.profile(userId).asLiveData()
        return profile
    }
}