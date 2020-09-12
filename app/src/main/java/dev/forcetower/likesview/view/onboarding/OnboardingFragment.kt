package dev.forcetower.likesview.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.likesview.databinding.FragmentOnboardingBinding
import dev.forcetower.toolkit.components.BaseFragment

@AndroidEntryPoint
class OnboardingFragment : BaseFragment(), OnboardingActions {
    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentOnboardingBinding.inflate(inflater, container, false).also {
            binding = it
            it.actions = this@OnboardingFragment
        }.root
    }

    override fun onMoveToLogin() {
        val directions = OnboardingFragmentDirections.actionOnboardingToAddProfile()
        findNavController().navigate(directions)
    }
}
