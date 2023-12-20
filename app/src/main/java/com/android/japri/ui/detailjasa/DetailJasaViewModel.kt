package com.android.japri.ui.detailjasa

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository

class DetailJasaViewModel(private val repository: AppRepository)  : ViewModel() {

    fun getUserById(id: String) = repository.getUserById(id)

}