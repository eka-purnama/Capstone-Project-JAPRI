package com.android.japri.ui.job

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.RequestBody

class JobViewModel(private val repository: AppRepository) : ViewModel() {

    fun getJobHistory(bodyRequest: RequestBody.JobHistoryRequest) = repository.getJobHistory(bodyRequest)

}
