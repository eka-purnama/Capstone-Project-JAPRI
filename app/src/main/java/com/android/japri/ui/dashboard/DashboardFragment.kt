package com.android.japri.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.japri.R
import com.android.japri.adapter.JobFieldsAdapter
import com.android.japri.data.JobField
import com.android.japri.databinding.FragmentDashboardBinding
import com.android.japri.preferences.UserSessionData
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.detailjasa.DetailJasaActivity
import com.android.japri.ui.login.LoginViewModel
import com.android.japri.ui.welcome.WelcomeScreenActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dashboardViewModel by viewModels<DashboardViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var adapter: JobFieldsAdapter
    private val list = ArrayList<JobField>()
    private var userSessionData: UserSessionData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        dashboardViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token.isEmpty()) {
                startActivity(Intent(requireContext(), WelcomeScreenActivity::class.java))
            } else {
                userSessionData = user
            }
        }

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

    private fun getJobFieldList(): ArrayList<JobField> {
        val jobFieldName = resources.getStringArray(R.array.job_fields)

        val jobFieldList = ArrayList<JobField>()
        for (i in jobFieldName.indices) {
            val touristDest = JobField(jobFieldName[i])
            jobFieldList.add(touristDest)
        }
        return jobFieldList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}