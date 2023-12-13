package com.android.japri.ui.detailjasa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.android.japri.data.repository.AppRepository
import com.android.japri.preferences.UserSessionData

class DetailJasaViewModel(private val repository: AppRepository)  : ViewModel() {

//    fun getSession(): LiveData<UserSessionData> {
//        return repository.getSession().asLiveData()
//    }
}