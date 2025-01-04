package ru.vsu.forum.features.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.vsu.forum.databinding.FragmentEditProfileBinding
import ru.vsu.forum.features.profile.data.UserRepository
import kotlin.text.isNullOrEmpty
import kotlin.toString

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModel()
    private val userRepository: UserRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val toolbar = binding.profileToolBar
        toolbar.setupWithNavController(findNavController())

        viewModel.name.observe(viewLifecycleOwner) {
            onNameChanged(it)
        }

        binding.profileUpdateButton.setOnClickListener {
            lifecycleScope.launch {
                userRepository.updateProfile(viewModel.name.value.toString(), viewModel.description.value)
                val profile = userRepository.getUserProfile()
                viewModel.setProfile(profile)
            }
        }

        lifecycleScope.launch {
            val profile = userRepository.getUserProfile()
            viewModel.setProfile(profile)
        }

        return binding.root
    }

    private  fun onNameChanged(name: String?){
        binding.profileUsernameTextInputLayout.error = if (name.isNullOrEmpty()) "Name is required" else null
    }
}