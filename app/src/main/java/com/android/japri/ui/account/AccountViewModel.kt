package com.android.japri.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.response.UserResponse
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AppRepository, id: String)  : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    init{
        getUserById(id)
    }

    private fun getUserById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val storyList = repository.getUserById(id)
                _user.value = storyList
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e("AccountViewModel", "Error: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}