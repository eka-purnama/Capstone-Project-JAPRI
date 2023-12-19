package com.android.japri.ui.accountsetting

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.request.AccountRequestBody

class AccountSettingViewModel(private val repository: AppRepository) : ViewModel() {

    fun getUserById(id: String) = repository.getUserById(id)

    fun editAccount(
        id: String,
        requestBody: AccountRequestBody
    ) = repository.editAccount(id, requestBody)

}