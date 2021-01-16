package dev.forcetower.likesview.view.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.core.source.repository.ProfileRepository
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
) : ViewModel(), HomeActions, MediaActions {
    val currentProfile = repository.currentProfile().asLiveData()
    val profiles = repository.profiles().asLiveData()

    private val _onAddProfile = MutableLiveData<Event<Unit>>()
    val onAddProfile: LiveData<Event<Unit>> = _onAddProfile

    private val _onProfileLongClicked = MutableLiveData<Event<InstagramProfile>>()
    val onProfileLongClicked: LiveData<Event<InstagramProfile>> = _onProfileLongClicked

    private val _onMediaClicked = MutableLiveData<Event<InstagramMedia>>()
    val onMediaClicked: LiveData<Event<InstagramMedia>> = _onMediaClicked

    override fun onReelClicked(profile: InstagramProfile) {
        setProfileSelected(profile)
    }

    override fun onReelLongClick(profile: InstagramProfile): Boolean {
        _onProfileLongClicked.value = Event(profile)
        return true
    }

    override fun onRemoveProfile(profile: InstagramProfile?) {
        profile ?: return
        viewModelScope.launch {
            val hasProfilesLeft = repository.onDeleteProfile(profile)
            if (!hasProfilesLeft) {
                _onAddProfile.value = Event(Unit)
            }
        }
    }

    override fun onAddProfile() {
        _onAddProfile.value = Event(Unit)
    }

    fun setProfileSelected(profile: InstagramProfile) {
        viewModelScope.launch {
            repository.markProfileSelected(profile)
        }
    }

    override fun onMediaClicked(media: InstagramMedia?) {
        media ?: return
        _onMediaClicked.value = Event(media)
    }
}
