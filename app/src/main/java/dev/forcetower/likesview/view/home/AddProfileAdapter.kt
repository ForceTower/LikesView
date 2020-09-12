package dev.forcetower.likesview.view.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.likesview.R
import dev.forcetower.likesview.databinding.ItemAddProfileButtonBinding
import dev.forcetower.toolkit.extensions.inflate

class AddProfileAdapter(
    private val actions: HomeActions
) : RecyclerView.Adapter<AddProfileAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProfileAdapter.Holder {
        return Holder(parent.inflate(R.layout.item_add_profile_button))
    }

    override fun onBindViewHolder(holder: AddProfileAdapter.Holder, position: Int) = Unit

    override fun getItemCount() = 1

    inner class Holder(val binding: ItemAddProfileButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        init { binding.actions = actions }
    }
}
