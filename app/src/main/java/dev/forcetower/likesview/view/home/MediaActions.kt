package dev.forcetower.likesview.view.home

import dev.forcetower.likesview.core.model.database.InstagramMedia

interface MediaActions {
    fun onMediaClicked(media: InstagramMedia?)
}