package ru.vsu.forum.features.topics.view

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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.vsu.forum.R
import ru.vsu.forum.databinding.FragmentTopicsBinding
import java.util.UUID
import kotlin.apply

class TopicsFragment : Fragment(), MenuProvider {
    private lateinit var binding: FragmentTopicsBinding

    private val viewModel: TopicsViewModel by viewModel()

    private lateinit var topicAdapter: TopicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopicsBinding.inflate(inflater, container, false)

        val menuHost = requireActivity()

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        topicAdapter = TopicAdapter { topicId ->
            navigateToTopicFragment(topicId)
        }

        binding.rvTopics.apply {
            adapter = topicAdapter
        }

        lifecycleScope.launch{
            viewModel.topicsFlow.collectLatest {
                pagedData -> topicAdapter.submitData(pagedData)
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

    private fun navigateToTopicFragment(topicId: UUID) {
        val action = TopicsFragmentDirections
            .actionNavigationHomeToNavigationTopic(topicId.toString())
        findNavController().navigate(action)
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