package dev.forcetower.likesview.view.medias

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.databinding.FragmentMediasBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class MediasFragment : BaseFragment() {
    private lateinit var binding: FragmentMediasBinding
    private lateinit var adapter: MediaDetailsAdapter

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}