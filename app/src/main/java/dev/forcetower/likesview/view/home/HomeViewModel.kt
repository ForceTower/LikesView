package dev.forcetower.likesview.view.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.source.repository.ProfileRepository
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
) : ViewModel(), HomeActions {
    val currentProfile = repository.currentProfile().asLiveData()
    val profiles = repository.profiles().asLiveData()

    override fun onReelClicked(profile: InstagramProfile) {
        setProfileSelected(profile)
    }

    fun setProfileSelected(profile: InstagramProfile) {
        viewModelScope.launch {
            repository.markProfileSelected(profile)
        }
    }
}