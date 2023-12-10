package com.android.japri.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.japri.data.repository.AppRepository
import com.android.japri.preferences.UserSessionData

class DashboardViewModel(private val repository: AppRepository)  : ViewModel() {

    fun getSession(): LiveData<UserSessionData> {
        return repository.getSession().asLiveData()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Beranda Fragment"
    }
    val text: LiveData<String> = _text
}