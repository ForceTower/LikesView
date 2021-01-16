package dev.forcetower.likesview.view.profile

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.likesview.R
import dev.forcetower.likesview.core.model.database.InstagramMedia
import dev.forcetower.likesview.databinding.ItemProfileHomeMediaBinding
import dev.forcetower.likesview.view.home.HomeActions
import dev.forcetower.likesview.view.home.MediaActions
import dev.forcetower.toolkit.extensions.inflate

class MediaAdapter(private val actions: MediaActions) : PagingDataAdapter<InstagramMedia, MediaAdapter.Holder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.item_profile_home_media), actions)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.media = getItem(position)
    }

    inner class Holder(val binding: ItemProfileHomeMediaBinding, actions: MediaActions) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
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
