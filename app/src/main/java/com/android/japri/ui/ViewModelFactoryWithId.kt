package com.android.japri.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.japri.data.repository.AppRepository
import com.android.japri.di.Injection
import com.android.japri.ui.account.AccountViewModel
import com.android.japri.ui.photoprofile.PhotoProfileViewModel

class ViewModelFactoryWithId(private val repository: AppRepository, private val id: String) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(repository, id) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryWithId? = null
        @JvmStatic
        fun getInstance(context: Context, id: String): ViewModelFactoryWithId {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryWithId::class.java) {
                    INSTANCE = ViewModelFactoryWithId(Injection.provideRepository(context), id)
                }
            }
            return INSTANCE as ViewModelFactoryWithId
        }
    }
}