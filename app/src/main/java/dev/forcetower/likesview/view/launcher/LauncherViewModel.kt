package dev.forcetower.likesview.view.launcher

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import dev.forcetower.toolkit.lifecycle.Event
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

class LauncherViewModel @ViewModelInject constructor() : ViewModel() {
    private val _launchDestination = MediatorLiveData<Event<LaunchDestination>>()
    val launchDestination: LiveData<Event<LaunchDestination>> = _launchDestination

    init {
        _launchDestination.value = Event(LaunchDestination.LOGIN)
    }
}

enum class LaunchDestination {
    HOME,
    LOGIN
}