package com.android.japri.ui.photoprofile

import androidx.lifecycle.ViewModel
import com.android.japri.data.repository.AppRepository
import java.io.File

class PhotoProfileViewModel(private val repository: AppRepository) : ViewModel() {

    fun editPhotoProfile(id: String, file: File) = repository.editPhotoProfile(id, file)

}