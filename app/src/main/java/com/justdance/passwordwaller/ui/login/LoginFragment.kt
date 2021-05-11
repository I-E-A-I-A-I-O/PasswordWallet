package com.justdance.passwordwaller.ui.login

import android.content.Intent
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
import com.justdance.passwordwaller.MainActivity
import com.justdance.passwordwaller.R
import com.justdance.passwordwaller.daoDb.AppDatabase
import com.justdance.passwordwaller.daoDb.entities.User
import com.justdance.passwordwaller.databinding.LoginFragmentBinding
import com.justdance.passwordwaller.network.ApiService
import com.justdance.passwordwaller.network.ErrorResponse
import com.justdance.passwordwaller.network.LoginInfo
import com.justdance.passwordwaller.network.UserInfo
import com.justdance.passwordwaller.redux.store
import com.justdance.passwordwaller.redux.user.SetUser
import com.justdance.passwordwaller.ui.passwordsRecycler.DataSource
import kotlinx.coroutines.launch
import java.util.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.registerQuestion.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        emailBindings()
        passwordBindings()
        form {
            useRealTimeValidation(500, true)
            inputLayout(binding.emailInputLayout) {
                isNotEmpty().description("Email required.")
                isEmail().description("Insert a valid email.")
            }
            inputLayout(binding.passwordInputLayout) {
                isNotEmpty().description("Password required.")
                length().atLeast(5).description("Password must be at least 5 characters long.")
                length().atMost(30).description("Password must bet at most 30 characters long.")
            }
            submitWith(binding.loginButton) {
                onSubmit()
            }
        }
        return binding.root
    }

    private fun onSubmit() {
        lifecycleScope.launch {
            toggleLoading()
            val apiService = ApiService()
            val requestBody =
                LoginInfo(viewModel.email.value!!.toLowerCase(Locale.ROOT), viewModel.password.value!!)
            apiService.login(requestBody) { userInfo: UserInfo?, errorResponse: ErrorResponse? ->
                toggleLoading()
                userInfo?.let { session ->
                    persistSession(session)
                    store.dispatch(SetUser(session))
                    context?.let { view?.let { it1 -> DataSource().loadPasswords(it1, lifecycleScope, it) } }
                    redirect()
                }
                errorResponse?.let { error ->
                    view?.let {
                        Snackbar.make(it, error.error, Snackbar.LENGTH_LONG).show()
                    }
                }
                if ((userInfo == null).and(errorResponse == null)) {
                    view?.let {
                        Snackbar.make(it, "Error signing in. Try again later.", Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        autoLogin()
    }

    private fun autoLogin() {
        lifecycleScope.launch {
            if (store.state.user?.email != null) {
                return@launch redirect()
            }
            val context = activity?.applicationContext ?: return@launch
            val users = AppDatabase.getInstance(context).userDao().getAll()
            if (users.isEmpty()) {
                return@launch
            }
            val userInfo = UserInfo(users[0].token, users[0].email)
            store.dispatch(SetUser(userInfo))
            view?.let { DataSource().loadPasswords(it, lifecycleScope, context) }
            redirect()
        }
    }

    private fun persistSession(userInfo: UserInfo) =
        lifecycleScope.launch {
            val isChecked = binding.rememberLoginSwitch.isChecked
            if (!isChecked) {
                return@launch
            }
            val context = activity?.applicationContext ?: return@launch
            val token = userInfo.token ?: return@launch
            val info = User( 1, userInfo.email, token)
            AppDatabase.getInstance(context).userDao().insertAll(info)
        }


    private fun toggleLoading() {
        binding.loginButton.isEnabled = !binding.loginButton.isEnabled
        binding.loadingIcon.visibility = when (binding.loadingIcon.isVisible) {
            true -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun redirect() {
        val intent = Intent(context, MainActivity::class.java)
        context?.startActivity(intent)
    }

    private fun emailBindings() {
        binding.emailInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun passwordBindings() {
        binding.passwordInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}