package com.justdance.passwordwaller.ui.passwordsRecycler

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.android.material.snackbar.Snackbar
import com.justdance.passwordwaller.daoDb.AppDatabase
import com.justdance.passwordwaller.daoDb.entities.Password
import com.justdance.passwordwaller.network.ApiService
import com.justdance.passwordwaller.network.ErrorResponse
import com.justdance.passwordwaller.network.PasswordInfo
import com.justdance.passwordwaller.redux.passwords.SetPasswords
import com.justdance.passwordwaller.redux.store
import kotlinx.coroutines.launch

class DataSource {
    fun loadPasswords(view: View, scope: LifecycleCoroutineScope, context: Context) {
        scope.launch {
            val apiService = ApiService()
            val authToken = store.state.user?.token
            apiService.getPasswords(authToken) { list: List<PasswordInfo>?, errorResponse: ErrorResponse? ->
                list?.let {
                    scope.launch {
                        val database = AppDatabase.getInstance(context)
                        database.passwordDao().deleteAllPasswords()
                        for (password in it) {
                            database.passwordDao().insertAll(Password(
                                password.passwordId,
                                password.description,
                                password.password
                            ))
                        }
                    }
                    store.dispatch(SetPasswords(it))
                }
                errorResponse?.let { error ->
                    setStoredToState(scope, context)
                    Snackbar.make(view, error.error, Snackbar.LENGTH_LONG).show()
                }
                if ((list == null).and(errorResponse == null)) {
                    setStoredToState(scope, context)
                    Snackbar.make(view, "Error retrieving the passwords. Try again later", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setStoredToState(scope: LifecycleCoroutineScope, context: Context) {
        scope.launch {
            val database = AppDatabase.getInstance(context)
            var passwords: List<PasswordInfo> = listOf()
            val storedPasswords = database.passwordDao().getAll()
            for (password in storedPasswords) {
                passwords = passwords.plus(PasswordInfo(password.passwordId, password.description, password.password))
            }
            store.dispatch(SetPasswords(passwords))
        }
    }
}