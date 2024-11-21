package ru.vsu.forum.ui.topic

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
import ru.vsu.forum.api.ForumApi
import ru.vsu.forum.data.model.SendMessageRequest
import ru.vsu.forum.data.source.MessageRepository
import ru.vsu.forum.databinding.FragmentTopicBinding
import ru.vsu.forum.utils.Config
import java.util.UUID

class TopicFragment : Fragment(), MenuProvider {

    private var _binding: FragmentTopicBinding? = null
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
        val args = TopicFragmentArgs.fromBundle(requireArguments())
        val topicId = UUID.fromString(args.topicId)

        val forumApi = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ForumApi::class.java)

        viewModel = ViewModelProvider.create(this, TopicViewModelFactory(forumApi, topicId))[TopicViewModel::class.java]

        _binding = FragmentTopicBinding.inflate(inflater, container, false)
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
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString()
            if (messageText.isBlank()) return@setOnClickListener

            lifecycleScope.launch {
                MessageRepository(viewModel.forumApi).sendMessage(viewModel.topicId, messageText)
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