package com.android.japri.ui.jobfield

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository

class JobFieldViewModel(private val repository: AppRepository) : ViewModel() {

    fun getJasaByField(field: String) = repository.getJasaByField(field)
}