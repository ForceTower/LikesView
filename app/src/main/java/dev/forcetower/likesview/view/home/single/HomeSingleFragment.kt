package dev.forcetower.likesview.view.home.single

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.R
import dev.forcetower.likesview.core.model.database.InstagramProfile
import dev.forcetower.likesview.databinding.FragmentHomeSingleBinding
import dev.forcetower.likesview.view.home.AddProfileAdapter
import dev.forcetower.likesview.view.home.HomeViewModel
import dev.forcetower.likesview.view.home.ReelsAdapter
import dev.forcetower.likesview.view.profile.HeaderAdapter
import dev.forcetower.likesview.view.profile.MediaAdapter
import dev.forcetower.likesview.view.profile.ProfileViewModel
import dev.forcetower.likesview.view.removeprofile.RemoveProfileSheet
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeSingleFragment : BaseFragment() {
    private var currentList: List<InstagramProfile> = emptyList()
    private lateinit var binding: FragmentHomeSingleBinding
    private val viewModel by activityViewModels<HomeViewModel>()
    private val profileViewModel by activityViewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentHomeSingleBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val add = AddProfileAdapter(viewModel)
        val reels = ReelsAdapter(viewModel)
        val merge = ConcatAdapter(add, reels)

        binding.run {
            recyclerReels.run {
                adapter = merge
                itemAnimator?.run {
                    changeDuration = 0
                }
            }
        }

        viewModel.currentProfile.observe(
            viewLifecycleOwner,
            {
                val same = binding.profile?.id == it?.id
                binding.profile = it
                if (it != null && !same) {
                    loadProfile(it)
                }
            }
        )

        viewModel.profiles.observe(
            viewLifecycleOwner,
            {
                currentList = it
                reels.submitList(it)
            }
        )

        viewModel.onAddProfile.observe(
            viewLifecycleOwner,
            EventObserver {
                val directions = HomeSingleFragmentDirections.actionHomeToAddProfile().apply {
                    fromHome = true
                }
                findNavController().navigate(directions)
            }
        )

        viewModel.onProfileLongClicked.observe(
            viewLifecycleOwner,
            EventObserver {
                val sheet = RemoveProfileSheet().apply {
                    arguments = bundleOf("profile" to it)
                }
                sheet.show(childFragmentManager, "remove_profile")
            }
        )
    }

    private fun loadProfile(profile: InstagramProfile) {
        Timber.d("Call to load profile... ${profile.username}")
        val medias = MediaAdapter()
        val header = HeaderAdapter()

        val adapter = ConcatAdapter(header, medias)

        val userId = profile.id
        lifecycleScope.launch {
            profileViewModel.media(userId).collectLatest {
                medias.submitData(it)
            }
            medias.loadStateFlow.collectLatest {
                if (it.refresh is LoadState.Error) {
                    showSnack(getString(R.string.failed_to_load_profile))
                }
            }
        }
        profileViewModel.profile(userId).observe(
            viewLifecycleOwner,
            {
                header.submitList(listOf(it))
            }
        )

        val columns = resources.getInteger(R.integer.home_profile_media_column)

        binding.recyclerMedias.adapter = adapter
        binding.recyclerMedias.run {
            itemAnimator?.run {
                changeDuration = 100L
            }
            (layoutManager as GridLayoutManager).run {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) columns else 1
                    }
                }
            }
        }
    }
}
