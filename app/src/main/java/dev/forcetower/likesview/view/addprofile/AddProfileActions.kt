package dev.forcetower.likesview.view.addprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.forcetower.likesview.core.model.dto.InstagramUserSearch

interface AddProfileActions {
    val username: MutableLiveData<String>
    val usernameError: LiveData<Int?>
    val loading: LiveData<Boolean>
    fun onAddProfileClick(user: InstagramUserSearch)
    fun onProfileAdded(username: String)
}