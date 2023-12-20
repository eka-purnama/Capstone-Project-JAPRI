package com.android.japri.ui.jasa

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.JasaRequestBody

class JasaViewModel(private val repository: AppRepository) : ViewModel() {

    fun getJasa() = repository.getJasa()

    fun searchJasa(requestBody: JasaRequestBody) = repository.searchJasa(requestBody)
}