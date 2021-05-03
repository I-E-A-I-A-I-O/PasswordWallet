package com.justdance.passwordwaller.redux.passwords

import com.justdance.passwordwaller.network.PasswordInfo

fun passwordReducer(state: List<PasswordInfo>, action: Any) =
    when(action) {
        is addPassword -> state.plus(action.password)
        is deletePassword -> state.filter {
            it.passwordId != action.passwordId
        }
        is setPasswords -> action.passwords
        else -> state
    }