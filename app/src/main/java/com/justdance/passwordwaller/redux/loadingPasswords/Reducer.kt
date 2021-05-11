package com.justdance.passwordwaller.redux.loadingPasswords

fun loadingPasswordReducer(state: Boolean, action: Any): Boolean =
    when(action) {
        is SetLoading -> action.state
        else -> state
    }