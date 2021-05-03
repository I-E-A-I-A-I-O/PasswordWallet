package com.justdance.passwordwaller.redux

import com.justdance.passwordwaller.network.PasswordInfo
import com.justdance.passwordwaller.network.UserInfo
import com.justdance.passwordwaller.redux.passwords.passwordReducer
import com.justdance.passwordwaller.redux.user.userReducer
import org.reduxkotlin.createThreadSafeStore

data class AppState(
    val user: UserInfo?,
    val passwords: List<PasswordInfo>
)

val INITIAL_STATE: AppState = AppState(
    user = null,
    passwords = listOf()
)

fun rootReducer(state: AppState, action: Any) = AppState(
    passwords = passwordReducer(state.passwords, action),
    user = userReducer(state.user, action)
)

val store = createThreadSafeStore(::rootReducer, INITIAL_STATE)