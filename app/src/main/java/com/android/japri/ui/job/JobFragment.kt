package com.android.japri.ui.job

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.japri.adapter.JobHistoryAdapter
import com.android.japri.data.ResultState
import com.android.japri.data.request.JobHistoryRequestBody
import com.android.japri.databinding.FragmentJobBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.ARG_POSITION
import com.android.japri.utils.EXTRA_USERNAME
import com.android.japri.utils.FINISH
import com.android.japri.utils.PROCESS
import com.android.japri.utils.showLoading

class JobFragment : Fragment() {

    private var _binding: FragmentJobBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<JobViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var position: Int? = null
    private var username: String? = null

    private lateinit var adapter: JobHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(EXTRA_USERNAME)
        }

        adapter = JobHistoryAdapter()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvJobHistory.layoutManager = layoutManager
        binding.rvJobHistory.adapter = adapter

        showHistoryData(username.toString())
    }

    private fun showHistoryData(username: String){
        if (position == 1){
            getJobHistory(JobHistoryRequestBody(PROCESS, username))
        } else {
            getJobHistory(JobHistoryRequestBody(FINISH, username))
        }
    }

    private fun getJobHistory(requestBody: JobHistoryRequestBody){
        viewModel.getJobHistory(requestBody).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.progressBar.showLoading(false)
                        if(result.data.isEmpty()){
                            binding.noDataFound.visibility = View.VISIBLE
                            adapter.submitList(null)
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        username?.let {
            showHistoryData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}