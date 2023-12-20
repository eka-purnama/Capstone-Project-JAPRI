package com.android.japri.ui.addjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.AddJobRequestBody
import com.android.japri.databinding.ActivityAddJobBinding
import com.android.japri.ui.PreferenceViewModel
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.DatePickerFragment
import com.android.japri.utils.EXTRA_USERNAME
import com.android.japri.utils.PROCESS
import com.android.japri.utils.TimePickerFragment
import com.android.japri.utils.setErrorTextAndColor
import com.android.japri.utils.showLoading
import com.android.japri.utils.showToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddJobActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener, DatePickerFragment.DialogDateListener {

    private lateinit var binding : ActivityAddJobBinding

    private lateinit var client: String
    private var serviceProvider: String? = null

    private val viewModel by viewModels<AddJobViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val preferenceViewModel by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_add_job_activity)

        serviceProvider = intent.getStringExtra(EXTRA_USERNAME)
        binding.edWorkerName.setText(serviceProvider)

        preferenceViewModel.getSession().observe(this) { user ->
            client = user.username
        }

        binding.btnAddJob.setOnClickListener {
            addJob()
        }
    }

    private fun addJob(){
        binding.apply {
            val jobName = edJobName.text.toString()
            val startDate = tvStartDate.text.toString()
            val endDate = tvEndDate.text.toString()
            val startTime = tvStartTime.text.toString()
            val endTime = tvEndTime.text.toString()
            val salary = edSalary.text.toString()
            val jobAddress = edJobAddress.text.toString()
            val detailJob = edDetailJob.text.toString()
            val status = PROCESS

            when {
                jobName.isEmpty() -> edJobName.error = getString(R.string.empty_input)
                startDate.isEmpty() -> tvStartDate.setErrorTextAndColor(getString(R.string.empty_text_view))
                endDate.isEmpty() -> tvEndDate.setErrorTextAndColor(getString(R.string.empty_text_view))
                startTime.isEmpty() -> tvStartTime.setErrorTextAndColor(getString(R.string.empty_text_view))
                endTime.isEmpty() -> tvEndTime.setErrorTextAndColor(getString(R.string.empty_text_view))
                salary.isEmpty() -> edSalary.error = getString(R.string.empty_input)
                jobAddress.isEmpty() -> edJobAddress.error = getString(R.string.empty_input)
                detailJob.isEmpty() -> edDetailJob.error = getString(R.string.empty_input)
                else -> {
                    val requestBody = AddJobRequestBody(
                        jobName, startDate, endDate, startTime, endTime, salary.toInt(), jobAddress,
                        detailJob, status, client, serviceProvider.toString(), null
                    )
                    add(requestBody)
                }
            }
        }
    }

    private fun add(requestBody: AddJobRequestBody){
        viewModel.addJob(requestBody).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.showLoading(true)
                    }
                    is ResultState.Success -> {
                        binding.progressBar.showLoading(false)
                        showToast(result.data.message.toString())
                        finish()
                    }
                    is ResultState.Error -> {
                        binding.progressBar.showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    fun showDatePicker(view: View) {
        val tag = when (view.id) {
            R.id.start_date_picker -> "start_date"
            R.id.end_date_picker -> "end_date"
            else -> throw IllegalArgumentException("Unknown view clicked")
        }
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        when (tag) {
            "start_date" -> findViewById<TextView>(R.id.tv_start_date).text = dateFormat.format(calendar.time)
            "end_date" -> findViewById<TextView>(R.id.tv_end_date).text = dateFormat.format(calendar.time)
        }
    }

    fun showTimePicker(view: View) {
        val tag = when (view.id) {
            R.id.start_time_picker -> "start_time"
            R.id.end_time_picker -> "end_time"
            else -> throw IllegalArgumentException("Unknown view clicked")
        }
        val dialogFragment = TimePickerFragment()
        dialogFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        if (tag != null) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            when (tag) {
                "start_time" -> findViewById<TextView>(R.id.tv_start_time).text = timeFormat.format(calendar.time)
                "end_time" -> findViewById<TextView>(R.id.tv_end_time).text = timeFormat.format(calendar.time)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}