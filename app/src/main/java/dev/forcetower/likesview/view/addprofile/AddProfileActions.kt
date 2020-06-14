package dev.forcetower.likesview.view.addprofile

import dev.forcetower.likesview.core.model.dto.InstagramUserSearch

interface AddProfileActions {
    fun onAddProfileClick(user: InstagramUserSearch)
}