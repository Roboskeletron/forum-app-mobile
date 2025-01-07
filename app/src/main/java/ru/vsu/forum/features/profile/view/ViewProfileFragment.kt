package ru.vsu.forum.features.profile.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.vsu.forum.databinding.FragmentViewProfileBinding
import ru.vsu.forum.features.common.domain.ImageService
import ru.vsu.forum.features.profile.data.UserRepository
import java.util.UUID

class ViewProfileFragment : Fragment() {
    private lateinit var binding: FragmentViewProfileBinding
    private val navArgs: ViewProfileFragmentArgs by navArgs()
    private val viewModel: ViewProfileViewModel by viewModels()

    private val userRepository: UserRepository by inject()
    private val imageService = ImageService(userRepository)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.viewProfileToolbar.setupWithNavController(findNavController())

        imageService.avatar.observe(viewLifecycleOwner) {
            viewModel.avatar.value = it
        }

        viewModel.avatar.observe(viewLifecycleOwner) {
            it?.also {
                binding.viewProfilePictureView.setImageBitmap(it)
            }
        }

        lifecycleScope.launch {
            val profile = userRepository.getById(UUID.fromString(navArgs.userId))

            if (profile == null) {
                Toast.makeText(context, "User profile not found", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                return@launch
            }

            imageService.fetchAvatar(profile.id)
            viewModel.setProfile(profile)
        }

        return binding.root
    }
}