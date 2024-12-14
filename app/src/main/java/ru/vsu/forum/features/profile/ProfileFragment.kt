package ru.vsu.forum.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.vsu.forum.databinding.FragmentProfileBinding
import ru.vsu.forum.R

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val toolbar = binding.profileToolBar
        toolbar.setupWithNavController(findNavController())

        viewModel.userProfile.observe(viewLifecycleOwner) {
            binding.viewModel = viewModel
        }

        viewModel.getProfile()
        return binding.root
    }
}