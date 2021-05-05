package com.justdance.passwordwaller.redux.user

import com.justdance.passwordwaller.network.UserInfo

data class SetUser(val userInfo: UserInfo)
class RemoveUser