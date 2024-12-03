package ru.vsu.forum.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.forum.R
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.data.source.UserRepository
import ru.vsu.forum.databinding.FragmentProfileBinding
import ru.vsu.forum.utils.Config

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forumService = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ForumService::class.java)

        lifecycleScope.launch {
            val user = UserRepository(forumService).getUserProfile()
            viewModel.setUser(user.getOrThrow())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}