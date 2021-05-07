package com.justdance.passwordwaller.ui.passwordsRecycler

import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.snackbar.Snackbar
import com.justdance.passwordwaller.network.ApiService
import com.justdance.passwordwaller.network.ErrorResponse
import com.justdance.passwordwaller.network.PasswordInfo
import com.justdance.passwordwaller.redux.loadingPasswords.SetLoading
import com.justdance.passwordwaller.redux.passwords.SetPasswords
import com.justdance.passwordwaller.redux.store
import kotlinx.coroutines.launch

class DataSource {
    fun loadPasswords(view: View, scope: LifecycleCoroutineScope) {
        scope.launch {
            store.dispatch(SetLoading(true))
            val apiService = ApiService()
            val authToken = store.state.user?.token
            apiService.getPasswords(authToken) { list: List<PasswordInfo>?, errorResponse: ErrorResponse? ->
                store.dispatch(SetLoading(false))
                list?.let {
                    store.dispatch(SetPasswords(it))
                }
                errorResponse?.let { error ->
                    Snackbar.make(view, error.error, Snackbar.LENGTH_LONG).show()
                }
                if ((list == null).and(errorResponse == null)) {
                    Snackbar.make(view, "Error retrieving the passwords. Try again later", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }
}