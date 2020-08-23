package dev.forcetower.likesview.view.addprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.databinding.FragmentAddProfileBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver

@AndroidEntryPoint
class AddProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentAddProfileBinding
    private lateinit var adapter: SearchAdapter
    private val viewModel by viewModels<AddProfileViewModel>()
    private val args by navArgs<AddProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = SearchAdapter(viewModel)
        return FragmentAddProfileBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onProfileClick.observe(viewLifecycleOwner, EventObserver {
            binding.etUsername.setText(it.username)
        })

        viewModel.onProfileAdded.observe(viewLifecycleOwner, EventObserver {
            val imm = requireContext().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            if (args.fromHome) {
                findNavController().popBackStack()
            } else {
                val directions = AddProfileFragmentDirections.actionAddProfileToHome()
                findNavController().navigate(directions)
            }
        })

        viewModel.search.observe(viewLifecycleOwner, Observer {
            binding.recyclerSearch.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
            adapter.submitList(it)
        })

        binding.recyclerSearch.run {
            adapter = this@AddProfileFragment.adapter
            itemAnimator?.run {
                changeDuration = 0L
            }
        }
    }
}