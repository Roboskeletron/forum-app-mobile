package ru.vsu.forum.ui.home

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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.vsu.forum.R
import ru.vsu.forum.api.ForumApi
import ru.vsu.forum.databinding.FragmentHomeBinding
import ru.vsu.forum.utils.Config
import java.util.UUID

class HomeFragment : Fragment(), MenuProvider {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    private lateinit var topicAdapter: TopicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val forumApi = Retrofit.Builder()
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ForumApi::class.java)

        viewModel = ViewModelProvider(this, HomeViewModelFactory(forumApi))[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val menuHost = requireActivity()

        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        lifecycleScope.launchWhenStarted {
            viewModel.topicsFlow.collectLatest { pagingData ->
                topicAdapter.submitData(pagingData)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        topicAdapter = TopicAdapter {
            topicId -> navigateToTopicFragment(topicId)
        }
        binding.rvTopics.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = topicAdapter
        }
    }

    private fun setupObservers() {
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
        val action = HomeFragmentDirections
            .actionNavigationHomeToNavigationTopic(topicId.toString())
        findNavController().navigate(action)
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