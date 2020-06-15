package dev.forcetower.likesview.core.source.paging

import androidx.paging.PagingSource
import com.google.gson.Gson
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.dto.InstagramProfilePartial
import dev.forcetower.likesview.core.source.local.LikeDB
import dev.forcetower.likesview.core.source.remote.InstagramAPI
import timber.log.Timber

class InstagramMediaSource(
    private val userId: Long,
    private val database: LikeDB,
    private val service: InstagramAPI
) : PagingSource<String, InstagramMedia>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, InstagramMedia> {
        return try {
            val next = params.key
            val medias = requestPage(userId, next)

            if (next == null) {
                val current = database.profile().getByIdDirect(userId)
                Timber.d("Loading for current... $current")
                if (current != null && (System.currentTimeMillis() - current.lastUpdate) > 300000) {
                    val page = service.getUser(current.username)
                    val profile = InstagramProfilePartial.createFromFetch(page)
                    Timber.d("Profile from fetch ${profile?.username} ${profile?.followersCount}")
                    if (profile != null) {
                        database.profile().update(profile)
                    }
                }
            }

            LoadResult.Page(
                medias,
                null,
                medias[0].nextPage
            )
        } catch (throwable: Throwable) {
            Timber.i(throwable, "Error loading")
            LoadResult.Error(throwable)
        }
    }

    private suspend fun requestPage(id: Long, maxId: String? = null): List<InstagramMedia> {
        val page = service.getProfilePage(createQueryMap(id, maxId = maxId))
        val user = page.data?.user ?: page.graph?.user
        val pageInfo = user?.edgeMedia?.pageInfo
        val nextPage = pageInfo?.endCursor
        return InstagramMedia.getMediaListFromProfileFetch(page, 120, nextPage)
    }

    private fun createQueryMap(
        userId: Long,
        amount: Int = 12,
        maxId: String? = null
    ): Map<String, String> {
        val gson = Gson()
        val variables = gson.toJson(
            mutableMapOf(
                "id" to userId,
                "first" to amount,
                "after" to maxId
            )
        )

        return mutableMapOf(
            "variables" to variables
        )
    }
}