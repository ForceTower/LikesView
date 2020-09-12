package dev.forcetower.likesview.core.source.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.gson.Gson
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.core.model.database.InstagramMediaPage
import dev.forcetower.likesview.core.source.local.LikeDB
import dev.forcetower.likesview.core.source.remote.InstagramAPI
import retrofit2.HttpException
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class InstagramMediaRemoteMediator(
    private val userId: Long,
    private val database: LikeDB,
    private val service: InstagramAPI
) : RemoteMediator<Int, InstagramMedia>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, InstagramMedia>
    ): MediatorResult {

        val page: String? = when (loadType) {
            // start from scratch
            LoadType.REFRESH -> {
                getRemoteKeyClosestToCurrentPosition(state)?.currentPageId
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
            // continue
            LoadType.APPEND -> {
                getRemoteKeyForLastItem(state)?.nextMaxId
            }
        }

        try {
            val response = service.getProfilePage(createQueryMap(userId, maxId = page))

            val user = response.data?.user ?: response.graph?.user
            val pageInfo = user?.edgeMedia?.pageInfo
            val nextPage = pageInfo?.endCursor
            val medias = InstagramMedia.getMediaListFromProfileFetch(response, 120)
            val endOfPaginationReached = medias.isEmpty()

            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    database.mediaPage().deleteAll()
                    database.media().deleteAll()
                }

                val keys = medias.map {
                    InstagramMediaPage(id = it.id, currentPageId = page, nextMaxId = nextPage)
                }
                database.mediaPage().insertAll(keys)
                database.media().insertAll(medias)
                Timber.d("Inserted ${medias.size} medias")
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: HttpException) {
            Timber.e(error, "Error during fetch")
            return MediatorResult.Error(error)
        } catch (error: Throwable) {
            Timber.e(error, "Error during fetch of page $page")
            return MediatorResult.Error(error)
        }
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

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, InstagramMedia>
    ): InstagramMediaPage? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { mediaId ->
                database.mediaPage().getPageForMedia(mediaId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, InstagramMedia>): InstagramMediaPage? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { media ->
                database.mediaPage().getPageForMedia(media.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, InstagramMedia>): InstagramMediaPage? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { media ->
                database.mediaPage().getPageForMedia(media.id)
            }
    }
}
