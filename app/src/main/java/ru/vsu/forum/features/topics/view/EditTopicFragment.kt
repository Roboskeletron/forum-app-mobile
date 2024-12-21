package ru.vsu.forum.features.topics.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.vsu.forum.R
import ru.vsu.forum.databinding.FragmentEditTopicBinding
import ru.vsu.forum.features.topics.data.TopicRepository
import ru.vsu.forum.features.topics.models.Topic
import java.util.UUID

class EditTopicFragment : Fragment() {
    private lateinit var binding: FragmentEditTopicBinding
    private lateinit var topic: Topic
    private val viewModel: EditTopicViewModel by viewModels()
    private val topicRepository: TopicRepository by inject()
    private val navArgs: EditTopicFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTopicBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val toolbar = binding.editTopicToolbar
        toolbar.setupWithNavController(findNavController())
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_create_topic -> {
                    updateTopic()
                    true
                }

                else -> false
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.editTopicTitleInputLayout.error = it
        }

        viewModel.title.observe(viewLifecycleOwner) {
            checkTopicTitle(it)
        }

        lifecycleScope.launch {
            topic = topicRepository.getById(UUID.fromString(navArgs.topicId))!!
            viewModel.title.value = topic.title
            viewModel.description.value = topic.description
        }

        return binding.root
    }

    private fun checkTopicTitle(title: String?) {
        if (title.isNullOrEmpty()) {
            viewModel.setError("Title cant be empty")
            return
        }

        lifecycleScope.launch {
            val isTitleUnique = topicRepository.isTitleUnique(title)

            val error = if (isTitleUnique || title == topic.title) null else "Title must be unique"
            viewModel.setError(error)
        }
    }

    private fun updateTopic() {
        if (!viewModel.error.value.isNullOrBlank()) {
            return
        }

        lifecycleScope.launch {
            topicRepository.updateTopic(topic.id, viewModel.title.value!!, viewModel.description.value)
            findNavController().navigateUp()
        }
    }
}