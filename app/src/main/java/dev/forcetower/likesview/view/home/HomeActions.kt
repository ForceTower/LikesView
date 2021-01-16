package dev.forcetower.likesview.view.home

import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramProfile

interface HomeActions {
    fun onReelClicked(profile: InstagramProfile)
    fun onReelLongClick(profile: InstagramProfile): Boolean
    fun onRemoveProfile(profile: InstagramProfile?)
    fun onAddProfile()
}
