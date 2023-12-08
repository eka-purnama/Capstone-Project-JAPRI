package com.android.japri.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.japri.R
import com.android.japri.adapter.JobFieldsAdapter
import com.android.japri.data.JobFields
import com.android.japri.databinding.FragmentDashboardBinding
import com.android.japri.ui.detailjasa.DetailJasaActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter: JobFieldsAdapter
    private val list = ArrayList<JobFields>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        adapter = JobFieldsAdapter()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvJobField.layoutManager = layoutManager
        binding.rvJobField.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.clear()
        list.addAll(getJobFieldList())
        adapter.submitList(list)

        binding.tvUsername.setOnClickListener{
            startActivity(Intent(requireContext(), DetailJasaActivity::class.java))
        }
    }

    private fun getJobFieldList(): ArrayList<JobFields> {
        val jobFieldName = resources.getStringArray(R.array.job_fields)

        val jobFieldList = ArrayList<JobFields>()
        for (i in jobFieldName.indices) {
            val touristDest = JobFields(jobFieldName[i])
            jobFieldList.add(touristDest)
        }
        return jobFieldList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}