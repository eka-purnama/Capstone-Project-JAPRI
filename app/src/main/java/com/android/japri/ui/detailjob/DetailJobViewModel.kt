package com.android.japri.ui.detailjob

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.AccountRequestBody
import com.android.japri.data.request.FeedbackRequestBody

class DetailJobViewModel(private val repository: AppRepository) : ViewModel() {

    fun getJobHistoryDetail(id: String) = repository.getJobHistoryDetail(id)

    fun finishTheJob(
        id: String,
        requestBody: FeedbackRequestBody
    ) = repository.finishTheJob(id, requestBody)
}