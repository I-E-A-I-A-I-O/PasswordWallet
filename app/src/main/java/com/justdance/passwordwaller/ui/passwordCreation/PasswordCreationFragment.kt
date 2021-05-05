package com.justdance.passwordwaller.ui.passwordCreation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.justdance.passwordwaller.databinding.FragmentCreationBinding
import com.justdance.passwordwaller.network.ApiService
import com.justdance.passwordwaller.network.NewPasswordInfo
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
    ): View? {
        binding = FragmentCreationBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        descriptionInputBindings()
        passwordInputBindings()
        confirmationInputBindings()
        binding.button.setOnClickListener {
            buttonPressed(it)
        }
        return binding.root
    }

    private fun buttonPressed(view: View) {
        lifecycleScope.launch {
            try {
                val requestBody = NewPasswordInfo(
                    binding.viewModel!!.description.value!!,
                    binding.viewModel!!.password.value!!,
                    binding.viewModel!!.confirmation.value!!
                )
                val reqApiService = ApiService()
            } catch (e: Exception) {
                Snackbar.make(view, "Error saving the password. Try again later.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun Fragment.hideKeyboard() {
        view?.let {
            activity?.hideKeyboard(it)
        }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun descriptionInputBindings() {
        binding.passwordDescription.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.viewModel!!.setDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.passwordDescription.setOnKeyListener { _, keyCode, event ->
            when {
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    hideKeyboard()
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }

    private fun passwordInputBindings() {
        binding.passwordInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.viewModel!!.setPassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.passwordInput.setOnKeyListener {_, keyCode, event ->
            when {
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    hideKeyboard()
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }

    private fun confirmationInputBindings() {
        binding.passwordInputConfirmation.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.viewModel!!.setConfirmation(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.passwordInputConfirmation.setOnKeyListener {_, keyCode, event ->
            when {
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {
                    hideKeyboard()
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }
}