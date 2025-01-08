package ru.vsu.forum.features.messages.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.vsu.forum.databinding.FragmentCommentListDialogBinding
import ru.vsu.forum.features.auth.domain.UserProvider
import ru.vsu.forum.features.profile.data.UserRepository

class CommentListDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCommentListDialogBinding
    private lateinit var commentAdapter: CommentAdapter
    private val viewModel: CommentListDialogViewModel by viewModel()
    private val userProvider: UserProvider by inject()
    private val userRepository: UserRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentListDialogBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        commentAdapter = CommentAdapter(
            this,
            userProvider,
            userRepository)

        lifecycleScope.launch {
            viewModel.commentsFlow.collectLatest {
                commentAdapter.submitData(it)
            }
        }

        return binding.root

    }
}