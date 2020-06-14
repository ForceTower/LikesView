package dev.forcetower.likesview.view.addprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.databinding.FragmentAddProfileBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class AddProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentAddProfileBinding
    private val viewModel by viewModels<AddProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAddProfileBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = viewModel
        }.root
    }
}