package com.android.japri.ui.jasa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JasaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Jasa Fragment"
    }
    val text: LiveData<String> = _text
}