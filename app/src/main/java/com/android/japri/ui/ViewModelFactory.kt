package com.android.japri.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.japri.data.repository.AppRepository
import com.android.japri.di.Injection
import com.android.japri.ui.account.AccountViewModel
import com.android.japri.ui.dashboard.DashboardViewModel
import com.android.japri.ui.detailjasa.DetailJasaViewModel
import com.android.japri.ui.login.LoginViewModel
import com.android.japri.ui.photoprofile.PhotoProfileViewModel
import com.android.japri.ui.register.RegisterViewModel

class ViewModelFactory(private val repository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PreferenceViewModel::class.java) -> {
                PreferenceViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailJasaViewModel::class.java) -> {
                DetailJasaViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PhotoProfileViewModel::class.java) -> {
                PhotoProfileViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}