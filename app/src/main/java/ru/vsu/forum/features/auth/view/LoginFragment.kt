package ru.vsu.forum.features.auth.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.vsu.forum.databinding.FragmentLoginBinding
import ru.vsu.forum.features.auth.domain.UserProvider

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModel()
    private val userProvider: UserProvider by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.loginLoginButton.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun login() {
        val passwordError = if (viewModel.password.value.isNullOrBlank())
            "Password is required"
            else null

        val usernameError = if (viewModel.username.value.isNullOrBlank())
            "Username is required"
            else null

        binding.loginUsernameInputLayout.error = usernameError
        binding.loginPasswordInputLayout.error = passwordError

        if (passwordError != null || usernameError != null) {
            return
        }

        lifecycleScope.launch {
            if (!userProvider.tryLogin(viewModel.username.value!!, viewModel.password.value!!)) {
                viewModel.showLoginError()
            }
            else {
                findNavController().navigateUp()
            }
        }
    }
}