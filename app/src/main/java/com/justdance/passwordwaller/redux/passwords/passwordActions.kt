package com.justdance.passwordwaller.redux.passwords

import com.justdance.passwordwaller.network.PasswordInfo

data class AddPassword(val password: PasswordInfo)
data class DeletePassword(val passwordId: String)
data class SetPasswords(val passwords: List<PasswordInfo>)