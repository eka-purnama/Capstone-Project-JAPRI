package com.android.japri.di

import android.content.Context
import com.android.japri.data.repository.AppRepository
import com.android.japri.data.retrofit.ApiConfig
import com.android.japri.preferences.UserPreference
import com.android.japri.preferences.dataStore

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(context)
        return AppRepository.getInstance(apiService, pref, context)
    }
}