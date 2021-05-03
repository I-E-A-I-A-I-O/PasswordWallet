package com.justdance.passwordwaller.redux.passwords

import com.justdance.passwordwaller.network.PasswordInfo

data class addPassword(val password: PasswordInfo)
data class deletePassword(val passwordId: String)
data class setPasswords(val passwords: List<PasswordInfo>)