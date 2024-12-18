package ru.vsu.forum.features.messages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.vsu.forum.R
import ru.vsu.forum.databinding.FragmentMessagesBinding
import ru.vsu.forum.features.messages.data.MessageRepository
import java.util.UUID
import kotlin.apply
import kotlin.text.isBlank
import kotlin.toString

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding

    private val args: MessagesFragmentArgs by navArgs()

    private val viewModel: MessagesViewModel by viewModel { parametersOf(UUID.fromString(args.topicId), args.topicTitle) }

    private val messageRepository: MessageRepository by inject()

    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        val toolbar = binding.messagesToolBar
        toolbar.setupWithNavController(findNavController())
        toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.action_profile -> {
                    findNavController().navigate(R.id.navigation_profile)
                    true
                }

                else -> false
            }
        }
        toolbar.setOnClickListener {
            val action = MessagesFragmentDirections.actionNavigationTopicToTopicInfoFragment(viewModel.topicId.toString())
            findNavController().navigate(action)
        }

        setupSearchView(toolbar.menu)
        setupMessageSending()
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        binding.messagesRecyclerView.apply {
            adapter = messageAdapter
        }

        lifecycleScope.launch {
            viewModel.messagesFlow.collectLatest { pagingData ->
                messageAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupMessageSending(){
        binding.messagesTextInputLayout.setEndIconOnClickListener {
            val messageText = binding.messagesTextInput.text.toString()

            if (messageText.isBlank()) return@setEndIconOnClickListener

            lifecycleScope.launch {
                // TODO: handle exception 
                messageRepository.sendMessage(viewModel.topicId, messageText)
                binding.messagesTextInput.text?.clear()
                viewModel.messagesFlow.collectLatest { pagingData ->
                    messageAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun setupSearchView(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "Search topics..."

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })
    }
}