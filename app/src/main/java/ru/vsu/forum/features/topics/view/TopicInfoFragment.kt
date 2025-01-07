package ru.vsu.forum.features.topics.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.vsu.forum.databinding.FragmentTopicInfoBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.common.domain.ImageService
import ru.vsu.forum.features.profile.data.UserRepository
import ru.vsu.forum.features.topics.data.TopicRepository
import java.util.UUID

class TopicInfoFragment : Fragment() {
    private  lateinit var binding: FragmentTopicInfoBinding
    private val args: TopicInfoFragmentArgs by navArgs()
    private val viewModel: TopicInfoViewModel by viewModels()
    private val topicRepository: TopicRepository by inject()
    private val userRepository: UserRepository by inject()
    private val userProvider: UserProvider by inject()
    private val imageService = ImageService(userRepository)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicInfoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val toolbar = binding.topicInfoToolbar
        toolbar.setupWithNavController(findNavController())

        binding.topicInfoEditFloatingButton.setOnClickListener {
            val action = TopicInfoFragmentDirections.actionTopicInfoFragmentToEditTopicFragment(args.topicId)
            findNavController().navigate(action)
        }

        imageService.avatar.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.topicInfoAuthorAvatar.setImageBitmap(it)
            }
        }

        lifecycleScope.launch {
            val topic = topicRepository.getById(UUID.fromString(args.topicId))

            if (topic == null) {
                Toast.makeText(context, "Unable to get topic info", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                return@launch
            }

            val author = userRepository.getById(topic.authorId)

            if (author == null) {
                Toast.makeText(context, "Unable to get topic author", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
                return@launch
            }

            imageService.fetchAvatar(author.id)

            viewModel.topic.value = topic
            viewModel.author.value = author
            viewModel.canEdit.value = userProvider.user.value?.id == author.id
        }

        return binding.root
    }
}