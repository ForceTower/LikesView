package dev.forcetower.likesview.core.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class InstagramMediaPage(
    @PrimaryKey
    val id: Int,
    val currentPageId: String?,
    val nextMaxId: String?,
    val previousPageId: String?
)
