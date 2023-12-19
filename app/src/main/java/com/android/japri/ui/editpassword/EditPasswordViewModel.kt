package com.android.japri.ui.editpassword

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.EditPasswordRequestBody

class EditPasswordViewModel(private val repository: AppRepository) : ViewModel() {
    fun editPassword(id: String, requestBody: EditPasswordRequestBody) = repository.editPassword(id, requestBody)
}