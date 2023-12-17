package com.android.japri.ui.detailjob

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository

class DetailJobViewModel(private val repository: AppRepository) : ViewModel() {

    fun getJobHistoryDetail(id: String) = repository.getJobHistoryDetail(id)

}