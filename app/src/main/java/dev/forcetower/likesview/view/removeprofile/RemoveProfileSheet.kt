package dev.forcetower.likesview.view.removeprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.databinding.SheetRemoveProfileBinding
import dev.forcetower.likesview.view.home.HomeViewModel
import dev.forcetower.toolkit.components.BaseSheetDialogFragment

@AndroidEntryPoint
class RemoveProfileSheet : BaseSheetDialogFragment() {
    private lateinit var binding: SheetRemoveProfileBinding
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return SheetRemoveProfileBinding.inflate(inflater, container, false).also {
            binding = it
            binding.actions = viewModel
            binding.profile = requireArguments().getParcelable("profile")
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNo.setOnClickListener { dismiss() }
        binding.btnYes.setOnClickListener {
            viewModel.onRemoveProfile(requireArguments().getParcelable("profile"))
            dismiss()
        }
    }
}
