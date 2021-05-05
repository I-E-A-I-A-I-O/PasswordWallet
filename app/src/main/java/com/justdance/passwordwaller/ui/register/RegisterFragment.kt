package com.justdance.passwordwaller.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.afollestad.vvalidator.form
import com.google.android.material.snackbar.Snackbar
import com.justdance.passwordwaller.R
import com.justdance.passwordwaller.databinding.RegisterFragmentBinding
import com.justdance.passwordwaller.network.ApiService
import com.justdance.passwordwaller.network.ErrorResponse
import com.justdance.passwordwaller.network.NewUser
import com.justdance.passwordwaller.network.UserInfo
import kotlinx.coroutines.launch
import java.util.*

class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: RegisterFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        emailInputBindings()
        passwordInputBindings()
        confirmInputBindings()
        form {
            useRealTimeValidation(500, true)
            inputLayout(binding.emailInputLayout) {
                isEmail().description("Insert a valid email")
                isNotEmpty().description("Email required")
            }
            inputLayout(binding.passwordInputLayout) {
                isNotEmpty().description("Password required")
                length().atLeast(5).description("Password must be at least 5 characters long")
                length().atMost(30).description("Password must be at most 30 characters long")
            }
            inputLayout(binding.confirmInputLayout) {
                isNotEmpty().description("Confirm your password")
                length().greaterThan(5).description("Password must be at least 5 characters long")
                length().atMost(30).description("Password must be at most 30 characters long")
            }
            submitWith(binding.registerButton) {
                onButtonPressed()
            }
        }
        return binding.root
    }

    private fun onButtonPressed() {
        lifecycleScope.launch {
            if (viewModel.password.value!! != viewModel.confirm.value!!) {
                view?.let {
                    Snackbar.make(it, "Passwords do not match.", Snackbar.LENGTH_SHORT).show()
                    return@launch
                }
            }
            toggleLoading()
            val requestBody = NewUser(
                viewModel.email.value!!.toLowerCase(Locale.ROOT),
                viewModel.password.value!!,
                viewModel.confirm.value!!
            )
            val apiService = ApiService()
            apiService.registerUser(requestBody) { userInfo: UserInfo?, errorResponse: ErrorResponse? ->
                toggleLoading()
                userInfo?.let {
                    view?.let {
                        Snackbar.make(it, "Account registered!", Snackbar.LENGTH_LONG).show()
                    }
                    redirect()
                }
                errorResponse?.let { err: ErrorResponse ->
                    view?.let {
                        Snackbar.make(it, err.error, Snackbar.LENGTH_LONG).show()
                    }
                }
                if ((errorResponse == null).and(userInfo == null)) {
                    view?.let {
                        Snackbar.make(it, "Error registering the account. Try again later", Snackbar.LENGTH_LONG).show()
                    }
                }
            }

        }
    }

    private fun redirect() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    private fun toggleLoading() {
        binding.progressBar.visibility = when (binding.progressBar.isVisible) {
            true -> View.GONE
            else -> View.VISIBLE
        }
        binding.registerButton.isEnabled = !binding.registerButton.isEnabled
    }

    private fun emailInputBindings() {
        binding.registerEmailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun passwordInputBindings() {
        binding.RegisterPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun confirmInputBindings() {
        binding.RegisterConfirmInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setConfirm(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}