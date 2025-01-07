package ru.vsu.forum.features.messages.view

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.messages.data.MessageRepository
import ru.vsu.forum.features.messages.models.Message
import ru.vsu.forum.features.profile.data.UserRepository
import java.util.UUID
import kotlin.apply

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding

    private val args: MessagesFragmentArgs by navArgs()

    private val viewModel: MessagesViewModel by viewModel { parametersOf(UUID.fromString(args.topicId), args.topicTitle) }

    private val messageRepository: MessageRepository by inject()

    private val userProvider: UserProvider by inject()

    private val userRepository: UserRepository by inject()

    private lateinit var messageAdapter: MessageAdapter

    private var sendMessageStrategy: suspend () -> Unit = {
        messageRepository.sendMessage(viewModel.topicId, viewModel.message.value!!)
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
                    findNavController().navigate(MessagesFragmentDirections.actionNavigationTopicToNavigationProfile())
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
        messageAdapter = MessageAdapter(this, userProvider, messageRepository, userRepository)
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
            val messageText = viewModel.message.value

            if (messageText.isNullOrBlank()) return@setEndIconOnClickListener

            lifecycleScope.launch {
                sendMessageStrategy()
                binding.messagesTextInput.text?.clear()
                viewModel.messagesFlow.collectLatest { pagingData ->
                    messageAdapter.submitData(pagingData)
                }
            }

            sendMessageStrategy = {
                messageRepository.sendMessage(viewModel.topicId, viewModel.message.value!!)
            }
        }

        userProvider.user.observe(viewLifecycleOwner) {
            binding.messagesTextInputLayout.isEnabled = it != null
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

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = MenuInflater(v.context)
        inflater.inflate(R.menu.message_context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedMessage = messageAdapter.getSelectedMessage() ?: return super.onContextItemSelected(item)

        return when (item.itemId) {
            R.id.action_edit -> {
                editMessage(selectedMessage)
                true
            }

            R.id.action_delete -> {
                deleteMessage(selectedMessage)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun editMessage(message: Message) {
        sendMessageStrategy = {
            messageRepository.updateMessage(message.id, viewModel.message.value!!)
        }

        binding.messagesTextInput.setText(message.text)
    }

    private fun deleteMessage(message: Message) {
        lifecycleScope.launch {
            messageRepository.deleteMessage(message.id)
            viewModel.messagesFlow.collectLatest { pagingData ->
                messageAdapter.submitData(pagingData)
            }
        }
    }
}