package dev.forcetower.likesview.view.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.likesview.R
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.databinding.ItemProfileReelBinding
import dev.forcetower.toolkit.extensions.inflate

class ProfilesAdapter(
    private val actions: HomeViewModel
) : ListAdapter<InstagramProfile, ProfilesAdapter.ProfileHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        return ProfileHolder(parent.inflate(R.layout.item_profile_reel))
    }

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        holder.binding.profile = getItem(position)
    }

    inner class ProfileHolder(val binding: ItemProfileReelBinding): RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback : DiffUtil.ItemCallback<InstagramProfile>() {
        override fun areItemsTheSame(
            oldItem: InstagramProfile,
            newItem: InstagramProfile
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: InstagramProfile,
            newItem: InstagramProfile
        ): Boolean {
            return oldItem == newItem
        }
    }
}
