package com.justdance.passwordwaller.ui.passwordCreation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.afollestad.vvalidator.form
import com.google.android.material.snackbar.Snackbar
import com.justdance.passwordwaller.databinding.FragmentCreationBinding
import com.justdance.passwordwaller.network.ApiService
import com.justdance.passwordwaller.network.ErrorResponse
import com.justdance.passwordwaller.network.NewPasswordInfo
import com.justdance.passwordwaller.network.PasswordInfo
import com.justdance.passwordwaller.redux.passwords.AddPassword
import com.justdance.passwordwaller.redux.store
import kotlinx.coroutines.launch

class PasswordCreationFragment : Fragment() {


    companion object {
        fun newInstance() = PasswordCreationFragment()
    }
    private lateinit var binding: FragmentCreationBinding
    private val viewModel: PasswordCreationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        descriptionInputBindings()
        passwordInputBindings()
        form {
            useRealTimeValidation(500, true)
            inputLayout(binding.descriptionInputLayout) {
                isNotEmpty().description("Insert a description.")
            }
            inputLayout(binding.passwordsInputLayout) {
                isNotEmpty().description("Insert a password.")
            }
            submitWith(binding.saveButton) {
                buttonPressed()
            }
        }
        return binding.root
    }

    private fun buttonPressed() {
        lifecycleScope.launch {
            val requestBody = NewPasswordInfo(
                binding.viewModel!!.description.value!!,
                binding.viewModel!!.password.value!!,
            )
            val apiService = ApiService()
            val token = store.state.user?.token
            apiService.addPassword(token, requestBody) { passwordInfo: PasswordInfo?, errorResponse: ErrorResponse? ->
                passwordInfo?.let { addedPass ->
                    store.dispatch(AddPassword(addedPass))
                    view?.let {
                        Snackbar.make(it, "Password saved.", Snackbar.LENGTH_SHORT).show()
                    }
                }
                errorResponse?.let { error ->
                    view?.let {
                        Snackbar.make(it, error.error, Snackbar.LENGTH_LONG).show()
                    }
                }
                if ((passwordInfo == null).and(errorResponse == null)) {
                    view?.let {
                        Snackbar.make(it, "Error saving the password. Try again later.", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun descriptionInputBindings() {
        binding.passwordDescription.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.viewModel!!.setDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun passwordInputBindings() {
        binding.passwordInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.viewModel!!.setPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}