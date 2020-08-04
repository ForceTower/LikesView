package dev.forcetower.likesview.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.R
import dev.forcetower.likesview.databinding.FragmentProfileBinding
import dev.forcetower.toolkit.components.BaseFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentProfileBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val medias = MediaAdapter()
        val header = HeaderAdapter()

        val adapter = ConcatAdapter(header, medias)

        val userId = requireArguments().getLong("userId")
        lifecycleScope.launch {
            viewModel.media(userId).collectLatest {
                medias.submitData(it)
            }
            medias.loadStateFlow.collectLatest {
                if (it.refresh is LoadState.Error) {
                    showSnack(getString(R.string.failed_to_load_profile))
                }
            }
        }
        viewModel.profile(userId).observe(viewLifecycleOwner, Observer {
            header.submitList(listOf(it))
        })

        val columns = resources.getInteger(R.integer.home_profile_media_column)

        binding.recyclerMedias.adapter = adapter
        binding.recyclerMedias.run {
            itemAnimator?.run {
                changeDuration = 0
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