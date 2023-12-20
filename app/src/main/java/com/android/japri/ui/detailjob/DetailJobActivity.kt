package com.android.japri.ui.detailjob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.Feedback
import com.android.japri.data.request.FeedbackRequestBody
import com.android.japri.data.response.JobHistoryResponseItem
import com.android.japri.databinding.ActivityDetailJobBinding
import com.android.japri.ui.PreferenceViewModel
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.FINISH
import com.android.japri.utils.SERVICE_PROVIDER
import com.android.japri.utils.convertTimestamp
import com.android.japri.utils.formatToRupiah
import com.android.japri.utils.showLoading
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
    private var role: String? = null
    private var jobStatus: String? = null
    private var userRating: Int? = null

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
                        showDetail(result.data.data?.get(0))
                        showDetailDifferentUser()
//                        showUserInfo()
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }

        binding.btnFinish.setOnClickListener {
            finishTheJob(id.toString())
        }

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            userRating = rating.toInt()
        }
    }

    private fun showDetail(job: JobHistoryResponseItem?) {
        val dateUpload = convertTimestamp(job?.createdAt?.seconds, job?.createdAt?.nanoseconds)
        jobStatus = job?.status.toString()

        binding.apply {
            tvJobName.text = job?.jobName
            tvUploadDate.text = getString(R.string.upload_date, dateUpload)
            tvJobAddress.text = job?.address
            tvJobDate.text = getString(R.string.job_date_time, job?.startDay, job?.endDay)
            tvJobTime.text = getString(R.string.job_date_time, job?.startTime, job?.endTime)
            tvSalary.text = job?.salary?.formatToRupiah()
            tvDetailJob.text = job?.description
            edReview.setText(job?.feedback?.comment)
            ratingBar.rating = job?.feedback?.rating?.toFloat() ?: 0f
        }

        if(role == SERVICE_PROVIDER){
            binding.userRole.text = getString(R.string.client)
            binding.tvUsername.text = job?.penggunaJasa
        } else {
            binding.userRole.text = getString(R.string.service_provider)
            binding.tvUsername.text = job?.penyediaJasa
        }
    }

//    photoUrl = result.data.photoUrl.toString()
//    if (photoUrl.isEmpty()){
//        binding.userPhoto.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.user_photo))
//    } else {
//        binding.userPhoto.loadImage(photoUrl)
//    }

    private fun showDetailDifferentUser() {
        if(role == SERVICE_PROVIDER || jobStatus == FINISH){
            binding.ratingBar.setIsIndicator(true)
            binding.edReview.isFocusable = false
            binding.edReview.isClickable = false
            binding.btnFinish.isVisible = false
        }
    }

    private fun finishTheJob(id: String){
        val review = binding.edReview.text.toString()

        if (userRating == null) {
            showToast(getString(R.string.empty_rating))
        } else if (review.isEmpty()) {
            binding.edReview.error = getString(R.string.empty_input)
        } else {
            val feedback = Feedback(review, userRating)
            val feedbackRequestBody = FeedbackRequestBody(feedback)
            viewModel.finishTheJob(id, feedbackRequestBody).observe(this) { result ->
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