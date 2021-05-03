package com.justdance.passwordwaller.ui.passwordCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PasswordCreationViewModel : ViewModel() {
    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description
    private val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password
    private val _confirmation = MutableLiveData<String>()
    val confirmation: LiveData<String>
        get() = _confirmation

    fun setDescription(description: String) {
        _description.value = description
    }
    fun setPassword(password: String) {
        _password.value = password
    }
    fun setConfirmation(confirmation: String) {
        _confirmation.value = confirmation
    }
}