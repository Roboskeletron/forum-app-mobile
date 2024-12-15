package ru.vsu.forum.features.topics.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.vsu.forum.R
import ru.vsu.forum.databinding.FragmentAddTopicBinding
import ru.vsu.forum.features.topics.data.TopicRepository

class AddTopicFragment : Fragment() {
    private lateinit var binding: FragmentAddTopicBinding
    private val viewModel: AddTopicViewModel by viewModels()
    private val topicRepository: TopicRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTopicBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val toolbar = binding.addTopicToolbar
        toolbar.setupWithNavController(findNavController())
        toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.action_create_topic -> {
                    createTopic()
                    true
                }
                else -> false
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.addTopicTitleInputLayout.error = it
        }

        viewModel.title.observe(viewLifecycleOwner) {
            checkTopicTitle(it)
        }

        return binding.root
    }

    private  fun createTopic(){
        if (viewModel.title.value.isNullOrEmpty()){
            return
        }

        lifecycleScope.launch {
            topicRepository.createTopic(viewModel.title.toString(), viewModel.description)
            findNavController().navigateUp()
        }
    }

    private  fun checkTopicTitle(title: String?){
        if (title.isNullOrEmpty()) {
            viewModel.setError("Title cant be empty")
            return
        }

        lifecycleScope.launch {
            val error = if (!topicRepository.isTitleUnique(title)) "Title must be unique" else null
            viewModel.setError(error)
        }
    }
}