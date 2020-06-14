package dev.forcetower.likesview.view.addprofile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.forcetower.likesview.core.model.dto.InstagramUserSearch

class AddProfileViewModel @ViewModelInject constructor(

) : ViewModel(), AddProfileActions {
    override val username = MutableLiveData("")
    override val usernameError = MutableLiveData<Int?>()
    override val loading = MutableLiveData(false)

    private val search = MediatorLiveData<List<InstagramUserSearch>>()

    override fun onAddProfileClick(user: InstagramUserSearch) {
        username.value = user.username
    }

    override fun onProfileAdded(username: String) {

    }
}