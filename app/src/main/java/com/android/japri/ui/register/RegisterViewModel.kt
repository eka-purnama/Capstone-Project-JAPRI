package com.android.japri.ui.register

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.RegisterRequestBody

class RegisterViewModel(private val repository: AppRepository) : ViewModel() {
    fun registerAccount (
        registerRequest: RegisterRequestBody
    ) = repository.registerAccount(registerRequest)
}