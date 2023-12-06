package com.android.japri.ui.addjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.japri.R

class AddJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_job)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_add_job_activity)

    }

    fun showDatePicker(view: View) {}
}