package com.android.japri.ui.detailjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.japri.R
import com.android.japri.databinding.ActivityDetailJobBinding

class DetailJobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailJobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_detail_job_activity)

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->

        }
    }
}