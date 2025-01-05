package ru.vsu.forum.features.auth.view

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.vsu.forum.databinding.FragmentRegisterBinding
import ru.vsu.forum.features.profile.data.UserRepository

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()
    private val userRepository: UserRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.registerToolbar.setupWithNavController(findNavController())

        binding.registerRegisterButton.setOnClickListener {
            lifecycleScope.launch {
                val passwordError =
                    "Password is required".takeIf { viewModel.password.value.isNullOrBlank() }

                val emailError = "Email address is required"
                    .takeIf { viewModel.email.value.isNullOrBlank() }
                    ?: "${viewModel.email.value} is not valid email address".takeUnless {
                        Patterns.EMAIL_ADDRESS.matcher(
                            viewModel.email.value!!
                        ).matches()
                    }
                    ?: "Email address ${viewModel.email.value} is already taken".takeIf {
                        userRepository.existsByEmail(viewModel.email.value!!)
                    }

                val usernameError = "Username is required"
                    .takeIf { viewModel.username.value.isNullOrBlank() }
                    ?: "Username ${viewModel.username.value} is already taken".takeIf {
                        userRepository.existsByUsername(viewModel.username.value!!)
                    }

                binding.registerUsernameInputLayout.error = usernameError
                binding.registerEmailInputLayout.error = emailError
                binding.registerPasswordInputLayout.error = passwordError

                if (usernameError != null || emailError != null || passwordError != null) {
                    return@launch
                }

                userRepository.register(
                    viewModel.username.value!!,
                    viewModel.email.value!!,
                    viewModel.password.value!!
                )

                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}