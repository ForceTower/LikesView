package dev.forcetower.likesview.core.model.dto

import com.google.gson.annotations.SerializedName

data class ProfileFetchResult(
    @SerializedName("graphql")
    val graph: GraphUser?,
    val data: GraphUser?
)

data class GraphUser(
    val user: ProfileUserGraph?
)

data class ProfileUserGraph(
    val id: Long,
    val username: String,
    val biography: String?,
    @SerializedName("full_name")
    val name: String?,
    @SerializedName("is_private")
    val private: Boolean,
    @SerializedName("is_verified")
    val verified: Boolean,
    @SerializedName("profile_pic_url")
    val pictureUrl: String?,
    @SerializedName("profile_pic_url_hd")
    val pictureUrlHd: String?,
    @SerializedName("edge_followed_by")
    val edgeFollowed: GraphEdge<Any>?,
    @SerializedName("edge_follow")
    val edgeFollow: GraphEdge<Any>?,
    @SerializedName("edge_owner_to_timeline_media")
    val edgeMedia: GraphEdge<MediaGraph>?
)

data class GraphEdge<T>(
    val count: Int?,
    @SerializedName("page_info")
    val pageInfo: EdgePageInfo?,
    val edges: List<GraphNode<T>>?
)

data class EdgePageInfo(
    @SerializedName("has_next_page")
    val hasNextPage: Boolean,
    @SerializedName("end_cursor")
    val endCursor: String?
)

data class GraphNode<T>(
    val node: T
)

data class MediaGraph(
    val id: String,
    @SerializedName("__typename")
    val type: String,
    @SerializedName("taken_at_timestamp")
    val takenAt: Long,
    @SerializedName("shortcode")
    val shortCode: String,
    @SerializedName("display_url")
    val displayUrl: String,
    @SerializedName("edge_media_preview_like")
    val likedEdge: GraphEdge<Any>?,
    @SerializedName("edge_media_to_caption")
    val captionEdge: GraphEdge<CaptionEdge>?,
    @SerializedName("media_preview")
    val mediaPreview: String?,
    @SerializedName("is_video")
    val video: Boolean,
    @SerializedName("accessibility_caption")
    val accessibilityCaption: String?,
    @SerializedName("thumbnail_resources")
    val thumbnailResources: List<ThumbnailResource>?,
    val dimensions: DimensionsResource?,
    val owner: Owner?
)

data class ThumbnailResource(
    val src: String,
    @SerializedName("config_width")
    val width: Int,
    @SerializedName("config_height")
    val height: Int
)

data class CaptionEdge(
    val text: String?
)

data class DimensionsResource(
    val width: Int,
    val height: Int
)

data class Owner(
    val id: Long,
    val username: String
)
