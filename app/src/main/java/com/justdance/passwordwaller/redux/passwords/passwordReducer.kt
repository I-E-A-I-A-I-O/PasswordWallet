package com.justdance.passwordwaller.redux.passwords

import com.justdance.passwordwaller.network.PasswordInfo

fun passwordReducer(state: List<PasswordInfo>, action: Any) =
    when(action) {
        is AddPassword -> state.plus(action.password)
        is DeletePassword -> state.filter {
            it.passwordId != action.passwordId
        }
        is SetPasswords -> action.passwords
        else -> state
    }