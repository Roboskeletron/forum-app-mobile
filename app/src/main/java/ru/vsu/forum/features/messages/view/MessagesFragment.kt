package ru.vsu.forum.features.messages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.forum.R
import ru.vsu.forum.data.source.MessageRepository
import ru.vsu.forum.databinding.FragmentMessagesBinding
import ru.vsu.forum.features.common.data.ForumService
import ru.vsu.forum.utils.Config
import java.util.UUID
import kotlin.apply
import kotlin.jvm.java
import kotlin.text.isBlank
import kotlin.toString

class MessagesFragment : Fragment(), MenuProvider {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TopicViewModel

    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = MessagesFragmentArgs.fromBundle(requireArguments())
        val topicId = UUID.fromString(args.topicId)

        val forumService = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ForumService::class.java)

        viewModel = ViewModelProvider.create(this,
            TopicViewModelFactory(forumService, topicId)
        )[TopicViewModel::class.java]

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val menuHost = requireActivity()

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        lifecycleScope.launchWhenStarted {
            viewModel.messagesFlow.collectLatest { pagingData ->
                messageAdapter.submitData(pagingData)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupMessageSending()
    }

    private fun setupObservers() {
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }

    private fun setupMessageSending(){
        binding.textInputLayout.setEndIconOnClickListener {
            val messageText = binding.messageTextInput.text.toString()

            if (messageText.isBlank()) return@setEndIconOnClickListener

            lifecycleScope.launch {
                MessageRepository(viewModel.forumService).sendMessage(viewModel.topicId, messageText)
                binding.messageTextInput.text?.clear()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
        setupSearchView(menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
        R.id.action_profile -> {
            findNavController().navigate(R.id.navigation_profile)
            true
        }

        else -> false
    }
}