package dev.forcetower.likesview.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.databinding.FragmentHomeBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

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

        val reels = ProfilesAdapter(viewModel)
//        val medias = MediasAdapter(viewModel)

        binding.run {
            recyclerReels.run {
                adapter = reels
                itemAnimator?.run {
                    changeDuration = 0
                }
            }
            recyclerMedias.run {
//                adapter = medias
            }
        }

        viewModel.currentProfile.observe(viewLifecycleOwner, Observer {
            binding.profile = it
        })

        viewModel.profiles.observe(viewLifecycleOwner, Observer {
            reels.submitList(it)
        })
    }
}