package com.android.japri.ui.detailjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.response.JobHistoryResponseItem
import com.android.japri.databinding.ActivityDetailJobBinding
import com.android.japri.ui.PreferenceViewModel
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.SERVICE_PROVIDER
import com.android.japri.utils.convertTimestamp
import com.android.japri.utils.formatToRupiah
import com.android.japri.utils.showToast

class DetailJobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailJobBinding

    private val viewModel by viewModels<DetailJobViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val preferenceViewModel by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var id: String? = null
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_detail_job_activity)

        id = intent.getStringExtra(EXTRA_ID)

        preferenceViewModel.getSession().observe(this) { user ->
            role = user.role
        }

        viewModel.getJobHistoryDetail(id.toString()).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                    }
                    is ResultState.Success -> {
                        showDetail(result.data.formJasaData)
                        showDetailDifferentUser(result.data.formJasaData)
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->

        }
    }

    private fun showDetail(job: JobHistoryResponseItem?) {
        val dateUpload = convertTimestamp(job?.createdAt?._seconds, job?.createdAt?._nanoseconds)

        binding.apply {
            tvJobName.text = job?.job_name
            tvUploadDate.text = getString(R.string.upload_date, dateUpload)
            tvJobAddress.text = job?.address
            tvJobDate.text = getString(R.string.job_date_time, job?.start_day, job?.end_day)
            tvJobTime.text = getString(R.string.job_date_time, job?.start_time, job?.end_time)
            tvSalary.text = job?.salary?.formatToRupiah()
            tvDetailJob.text = job?.description
        }
    }

    private fun showDetailDifferentUser(job: JobHistoryResponseItem?) {
        when(role){
            SERVICE_PROVIDER -> {
                binding.ratingBar.setIsIndicator(true)
                binding.edReview.isFocusable = false
                binding.edReview.isClickable = false
                binding.btnFinish.isVisible = false
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