package dev.forcetower.likesview.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.databinding.FragmentHomeBinding
import dev.forcetower.likesview.view.profile.ProfileFragmentAdapter
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var currentList: List<InstagramProfile> = emptyList()
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentHomeBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reels = ReelsAdapter(viewModel)
        val profiles = ProfileFragmentAdapter(childFragmentManager, lifecycle)

        binding.run {
            recyclerReels.run {
                adapter = reels
                itemAnimator?.run {
                    changeDuration = 0
                }
            }
            viewPager.run {
                adapter = profiles
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        if (position < currentList.size) {
                            viewModel.setProfileSelected(currentList[position])
                            recyclerReels.scrollToPosition(position)
                        }
                    }
                })
            }
        }

        viewModel.currentProfile.observe(viewLifecycleOwner, Observer {
            binding.profile = it
            val position = currentList.indexOfFirst { el -> el.username == it?.username }
            if (position != -1) {
                binding.viewPager.setCurrentItem(position, true)
            }
        })

        viewModel.profiles.observe(viewLifecycleOwner, Observer {
            currentList = it
            reels.submitList(it)
            profiles.submitList(it.map { user -> user.id })
        })
    }
}