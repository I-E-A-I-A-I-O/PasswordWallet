package com.justdance.passwordwaller.redux.user

import com.justdance.passwordwaller.network.UserInfo

fun userReducer(state: UserInfo?, action: Any) =
    when(action) {
        is setUser -> action.userInfo
        is removeUser -> null
        else -> state
    }