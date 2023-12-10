package com.android.japri.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.japri.data.repository.AppRepository
import kotlinx.coroutines.launch

class AccountViewModel(private val repository: AppRepository)  : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Akun Fragment"
    }
    val text: LiveData<String> = _text
}