package dev.forcetower.likesview.view.medias

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.likesview.R
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.databinding.ItemMediaDetailsSingleBinding
import dev.forcetower.toolkit.extensions.inflate

class MediaDetailsAdapter : PagingDataAdapter<InstagramMedia, MediaDetailsAdapter.MediaHolder>(DiffCallback) {
    override fun onBindViewHolder(holder: MediaHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is MediaHolder.ImageHolder -> {
                holder.binding.media = item
            }
            is MediaHolder.VideoHolder -> {
                holder.binding.media = item
            }
            is MediaHolder.SideHolder -> {
                holder.binding.media = item
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaHolder {
        return when (viewType) {
            0 -> MediaHolder.ImageHolder(parent.inflate(R.layout.item_media_details_single))
            1 -> MediaHolder.VideoHolder(parent.inflate(R.layout.item_media_details_single))
            2 -> MediaHolder.SideHolder(parent.inflate(R.layout.item_media_details_single))
            else -> throw IllegalStateException("no view defined for type $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.type) {
            "GraphImage" -> 0
            "GraphVideo" -> 1
            "GraphSidecar" -> 2
            else -> 0
        }
    }

    sealed class MediaHolder(view: View) : RecyclerView.ViewHolder(view) {
        class ImageHolder(val binding: ItemMediaDetailsSingleBinding) : MediaHolder(binding.root)
        class VideoHolder(val binding: ItemMediaDetailsSingleBinding) : MediaHolder(binding.root)
        class SideHolder(val binding: ItemMediaDetailsSingleBinding) : MediaHolder(binding.root)
    }

    private object DiffCallback : DiffUtil.ItemCallback<InstagramMedia>() {
        override fun areItemsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InstagramMedia, newItem: InstagramMedia): Boolean {
            return oldItem == newItem
        }
    }
}