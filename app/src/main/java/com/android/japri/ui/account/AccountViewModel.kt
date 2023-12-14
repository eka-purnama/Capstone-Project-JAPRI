package com.android.japri.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.japri.data.repository.AppRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AppRepository)  : ViewModel() {

    fun getUserById(id: String) = repository.getUserById(id)

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}