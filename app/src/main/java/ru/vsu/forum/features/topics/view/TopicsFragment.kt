package ru.vsu.forum.features.topics.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.vsu.forum.R
import ru.vsu.forum.databinding.FragmentTopicsBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.topics.models.Topic
import kotlin.apply

class TopicsFragment : Fragment() {
    private lateinit var binding: FragmentTopicsBinding

    private val viewModel: TopicsViewModel by viewModel()
    private val userProvider: UserProvider by inject()

    private lateinit var topicAdapter: TopicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicsBinding.inflate(inflater, container, false)

        val toolbar = binding.topicsToolbar

        toolbar.setupWithNavController(findNavController())
        toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.action_profile -> {
                    findNavController().navigate(TopicsFragmentDirections.actionNavigationHomeToNavigationProfile())
                    true
                }
                else -> false
            }
        }
        setupSearchView(toolbar.menu)

        binding.topicsAddTopicFab.setOnClickListener {
            findNavController().navigate(TopicsFragmentDirections.actionNavigationHomeToAddTopicFragment())
        }

        userProvider.user.observe(viewLifecycleOwner) {
            binding.topicsAddTopicFab.isEnabled = it != null
        }

        topicAdapter = TopicAdapter { topic ->
            navigateToTopicFragment(topic)
        }

        binding.topicsRecyclerView.apply {
            adapter = topicAdapter
        }

        lifecycleScope.launch{
            viewModel.topicsFlow.collectLatest {
                    pagedData -> topicAdapter.submitData(pagedData)
            }
        }

        return binding.root
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

    private fun navigateToTopicFragment(topic: Topic) {
        val action = TopicsFragmentDirections
            .actionNavigationHomeToNavigationTopic(topic.id.toString(), topic.title)
        findNavController().navigate(action)
    }
}