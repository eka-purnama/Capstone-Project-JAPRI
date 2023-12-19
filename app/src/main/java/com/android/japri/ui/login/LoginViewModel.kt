package com.android.japri.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.LoginRequestBody
import com.android.japri.preferences.UserSessionData
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AppRepository) : ViewModel() {

    fun login(loginRequest: LoginRequestBody) = repository.loginAccount(loginRequest)

    fun saveSession(userData: UserSessionData) {
        viewModelScope.launch {
            repository.saveSession(userData)
            Log.d(TAG, "Token saved: ${userData.token}")
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}