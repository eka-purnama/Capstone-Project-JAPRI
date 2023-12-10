package com.android.japri.ui.addjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.android.japri.R
import com.android.japri.databinding.ActivityAddJobBinding
import com.android.japri.ui.customview.DateTimeTextView

class AddJobActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddJobBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_add_job_activity)

        binding.btnAddJob.setOnClickListener {
            if (binding.tvStartDate.text.isNullOrEmpty()) {
                binding.tvStartDate.setErrorTextAndColor("Mohon diisi", R.color.orange)
            }
        }
    }

    fun showDatePicker(view: View) {}
}