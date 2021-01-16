package dev.forcetower.likesview.view.medias

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.databinding.FragmentMediasBinding
import dev.forcetower.toolkit.components.BaseFragment
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MediasFragment : BaseFragment() {
    private lateinit var binding: FragmentMediasBinding
    private lateinit var adapter: MediaDetailsAdapter
    private val viewModel by viewModels<MediasViewModel>()
    private val args by navArgs<MediasFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentMediasBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        adapter = MediaDetailsAdapter()

        binding.mediasRecycler.apply {
            adapter = this@MediasFragment.adapter
            itemAnimator?.apply {
                changeDuration = 0L
                addDuration = 0L
                moveDuration = 0L
                removeDuration = 0L
            }
        }

        return view
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.medias(args.profileId, args.mediaIdStart).collectLatest {
                adapter.submitData(it)
            }
        }
        viewModel.profile(args.profileId).observe(viewLifecycleOwner) {
            binding.profile = it
        }
    }
}