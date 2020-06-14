package dev.forcetower.likesview.core.model.dto

import dev.forcetower.likesview.core.model.database.InstagramProfile
import java.util.Calendar

data class InstagramProfilePartial(
    val id: Long,
    val username: String,
    val name: String?,
    val pictureUrl: String?,
    val pictureUrlHd: String?,
    val biography: String?,
    val followingCount: Int,
    val followersCount: Int,
    val postCount: Int,
    val private: Boolean,
    val verified: Boolean,
    val nextCachedPage: String?,
    val hasCachedNextPage: Boolean,
    val lastUpdate: Long
) {
    companion object {
        fun createFromFetch(fetchResult: ProfileFetchResult): InstagramProfilePartial? {
            val user = fetchResult.graph?.user
            user ?: return null
            return InstagramProfilePartial(
                user.id,
                user.username,
                user.name,
                user.pictureUrl,
                user.pictureUrlHd,
                user.biography,
                user.edgeFollow?.count ?: -1,
                user.edgeFollowed?.count ?: -1,
                user.edgeMedia?.count ?: -1,
                user.private,
                user.verified,
                user.edgeMedia?.pageInfo?.endCursor,
                user.edgeMedia?.pageInfo?.hasNextPage ?: false,
                Calendar.getInstance().timeInMillis
            )
        }
    }
}