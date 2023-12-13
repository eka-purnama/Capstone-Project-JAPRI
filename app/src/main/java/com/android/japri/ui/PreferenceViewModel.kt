package com.android.japri.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.japri.data.repository.AppRepository
import com.android.japri.preferences.UserSessionData

class PreferenceViewModel(private val repository: AppRepository) : ViewModel() {

    fun getSession(): LiveData<UserSessionData> {
        return repository.getSession().asLiveData()
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}