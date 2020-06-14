package dev.forcetower.likesview.view.addprofile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.likesview.R
import dev.forcetower.likesview.core.model.dto.InstagramUserSearch
import dev.forcetower.likesview.databinding.ItemAddProfileBinding
import dev.forcetower.toolkit.extensions.inflate

class SearchAdapter(
    private val actions: AddProfileActions
) : ListAdapter<InstagramUserSearch, SearchAdapter.UserHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(parent.inflate(R.layout.item_add_profile))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.binding.user = getItem(position)
    }

    inner class UserHolder(val binding: ItemAddProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }

    private object DiffCallback: DiffUtil.ItemCallback<InstagramUserSearch>() {
        override fun areItemsTheSame(
            oldItem: InstagramUserSearch,
            newItem: InstagramUserSearch
        ): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(
            oldItem: InstagramUserSearch,
            newItem: InstagramUserSearch
        ): Boolean {
            return oldItem == newItem
        }
    }
}
