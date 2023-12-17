package com.android.japri.ui.jasa

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.RequestBody
import com.android.japri.data.response.JobHistoryResponseItem
import kotlinx.coroutines.launch

class JasaViewModel(private val repository: AppRepository) : ViewModel() {
    //    private val _text = MutableLiveData<String>().apply {
    //        value = "Jasa Fragment"
    //    }
    //    val text: LiveData<String> = _text
}