package dev.forcetower.likesview.view.profile

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ProfileFragmentAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {
    private val differ = AsyncListDiffer(this, DiffCallback)

    fun submitList(next: List<Long>) {
        differ.submitList(next)
    }

    override fun getItemCount() = differ.currentList.size

    @ExperimentalCoroutinesApi
    override fun createFragment(position: Int): Fragment {
        return ProfileFragment().apply {
            arguments = bundleOf("userId" to differ.currentList[position])
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Long>() {
        override fun areItemsTheSame(oldItem: Long, newItem: Long) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Long, newItem: Long) = oldItem == newItem
    }
}
