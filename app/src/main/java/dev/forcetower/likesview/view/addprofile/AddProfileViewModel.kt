package dev.forcetower.likesview.view.addprofile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.forcetower.likesview.R
import dev.forcetower.likesview.core.model.dto.InstagramUserSearch
import dev.forcetower.likesview.core.source.repository.ProfileRepository
import dev.forcetower.toolkit.lifecycle.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class AddProfileViewModel @ViewModelInject constructor(
    private val repository: ProfileRepository
) : ViewModel(), AddProfileActions {
    override val username = MutableLiveData("")
    override val usernameError = MutableLiveData<Int?>()
    override val loading = MutableLiveData(false)
    private var _current: Job? = null

    private var _selectedProfile: InstagramUserSearch? = null

    private val _search = MediatorLiveData<List<InstagramUserSearch>>()
    val search: LiveData<List<InstagramUserSearch>> = _search

    private val _onProfileClick = MediatorLiveData<Event<InstagramUserSearch>>()
    val onProfileClick: LiveData<Event<InstagramUserSearch>> = _onProfileClick

    private val _onProfileAdded = MediatorLiveData<Event<String>>()
    val onProfileAdded: LiveData<Event<String>> = _onProfileAdded

    init {
        _search.addSource(username) {
            Timber.d("Username changed value to $it")
            val name = if (it.startsWith("@")) it.substring(1) else it
            _current?.cancel()

            _current = viewModelScope.launch {
                val current = repository.search(name)
                _search.value = current
            }
        }
    }

    override fun onAddProfileClick(user: InstagramUserSearch) {
        _selectedProfile = user

        viewModelScope.launch {
            repository.addProfile(user)
            _onProfileAdded.value = Event(user.username)
        }
    }

    override fun onProfileAdded(username: String) {
        val profile = _selectedProfile
        if (profile == null || profile.username != username) {
            usernameError.value = R.string.no_profile_selected
            return
        }
        viewModelScope.launch {
            repository.addProfile(profile)
            _onProfileAdded.value = Event(username)
        }
    }
}
