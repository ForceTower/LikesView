package dev.forcetower.likesview.view.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.likesview.R
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.databinding.ItemProfileHeaderBinding
import dev.forcetower.toolkit.extensions.inflate

class HeaderAdapter : ListAdapter<InstagramProfile, HeaderAdapter.Holder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(R.layout.item_profile_header))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.profile = getItem(position)
    }

    inner class Holder(val binding: ItemProfileHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<InstagramProfile>() {
        override fun areItemsTheSame(
            oldItem: InstagramProfile,
            newItem: InstagramProfile
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: InstagramProfile,
            newItem: InstagramProfile
        ) = oldItem == newItem
    }
}
