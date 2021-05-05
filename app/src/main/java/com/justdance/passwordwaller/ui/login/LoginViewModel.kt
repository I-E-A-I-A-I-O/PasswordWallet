package com.justdance.passwordwaller.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email
    private val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    fun setEmail(value: String) {
        _email.value = value
    }
    fun setPassword(value: String) {
        _password.value = value
    }
}