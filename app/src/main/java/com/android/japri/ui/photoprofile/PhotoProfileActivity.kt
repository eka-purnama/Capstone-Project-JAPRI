package com.android.japri.ui.photoprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.japri.R
import com.android.japri.databinding.ActivityPhotoProfileBinding
class PhotoProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhotoProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_photo_profile_activity)
    }
}