package com.android.japri.ui.addjob

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.AddJobRequestBody

class AddJobViewModel(private val repository: AppRepository) : ViewModel() {
    fun addJob (
        requestBody: AddJobRequestBody
    ) = repository.addJob(requestBody)
}