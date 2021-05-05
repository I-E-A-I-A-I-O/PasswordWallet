package com.justdance.passwordwaller.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email
    private val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password
    private val _confirm = MutableLiveData<String>()
    val confirm: LiveData<String>
        get() = _confirm
    fun setEmail(value: String) {
        _email.value = value
    }
    fun setPassword(value: String) {
        _password.value = value
    }
    fun setConfirm(value: String) {
        _confirm.value = value
    }
}