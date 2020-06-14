package dev.forcetower.likesview.view.launcher

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.forcetower.likesview.core.source.repository.ProfileRepository
import dev.forcetower.toolkit.lifecycle.Event
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

class LauncherViewModel @ViewModelInject constructor(
    repository: ProfileRepository
) : ViewModel() {
    private val _launchDestination = MediatorLiveData<Event<LaunchDestination>>()
    val launchDestination: LiveData<Event<LaunchDestination>> = _launchDestination

    init {
        val source = repository.profilesCount().asLiveData()
        _launchDestination.addSource(source) {
            _launchDestination.removeSource(source)
            _launchDestination.value = Event(
                if (it == 0) LaunchDestination.LOGIN else LaunchDestination.HOME
            )
        }
    }
}

enum class LaunchDestination {
    HOME,
    LOGIN
}