package com.android.japri.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.japri.R
import com.android.japri.adapter.JobFieldsAdapter
import com.android.japri.data.JobField
import com.android.japri.databinding.FragmentDashboardBinding
import com.android.japri.ui.PreferenceViewModel
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.detailjasa.DetailJasaActivity
import com.android.japri.ui.welcome.WelcomeScreenActivity
import com.android.japri.utils.SERVICE_PROVIDER

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val preferenceViewModel by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private lateinit var adapter: JobFieldsAdapter
    private val list = ArrayList<JobField>()

    private lateinit var role: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        adapter = JobFieldsAdapter()

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvJobField.layoutManager = layoutManager
        binding.rvJobField.adapter = adapter

        list.clear()
        list.addAll(getJobFieldList())
        adapter.submitList(list)

        preferenceViewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (user.token.isEmpty()) {
                startActivity(Intent(requireContext(), WelcomeScreenActivity::class.java))
            } else {
                role = user.role
                binding.tvUsername.text = getString(R.string.dashboard_username, user.username)
                showUI()
            }
        }

        binding.tvUsername.setOnClickListener{
            startActivity(Intent(requireContext(), DetailJasaActivity::class.java))
        }
    }

    private fun showUI() {
        binding.dashboardPicture.setImageResource(
            if (role == SERVICE_PROVIDER) R.drawable.dashboard_picture_service_provider
            else R.drawable.dashboard_picture_client
        )
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