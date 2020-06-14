package dev.forcetower.likesview.view.home

import dev.forcetower.likesview.core.model.database.InstagramProfile

interface HomeActions {
    fun onReelClicked(profile: InstagramProfile)
}