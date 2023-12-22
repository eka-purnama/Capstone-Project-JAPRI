package com.android.japri.ui.jobfield

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.japri.adapter.JasaAdapter
import com.android.japri.data.ResultState
import com.android.japri.databinding.ActivityJobFieldBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.EXTRA_NAME
import com.android.japri.utils.showLoading
import com.android.japri.utils.showToast

class JobFieldActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobFieldBinding

    private val viewModel by viewModels<JobFieldViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var jobFieldName: String? = null

    private lateinit var adapter: JasaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        jobFieldName = intent.getStringExtra(EXTRA_NAME)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = jobFieldName

        adapter = JasaAdapter()

        binding.rvJasa.layoutManager = LinearLayoutManager(this)
        binding.rvJasa.adapter = adapter

        getJasaByField(jobFieldName.toString())
    }

    private fun getJasaByField(field: String){
        viewModel.getJasaByField(field).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.progressBar.showLoading(false)
                        if(result.data.isEmpty()){
                            binding.noDataFound.visibility = View.VISIBLE
                        } else {
                            binding.noDataFound.visibility = View.INVISIBLE
                            adapter.submitList(result.data)
                        }
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        binding.progressBar.showLoading(false)
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