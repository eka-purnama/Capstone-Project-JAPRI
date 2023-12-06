package com.android.japri.ui.job

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.japri.R
import com.android.japri.databinding.FragmentJobBinding
import com.android.japri.ui.detailjasa.DetailJasaActivity
import com.android.japri.ui.detailjob.DetailJobActivity
import com.android.japri.ui.main.MainActivity

class JobFragment : Fragment() {

    private lateinit var binding: FragmentJobBinding
    private lateinit var viewModel: JobViewModel

    private var position: Int? = null
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobBinding.inflate(inflater, container, false)

        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(JobViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_STATUS)
        }

        if (position == 1){
            binding.status.text = "Pekerjaan diproses"
        } else {
            binding.status.text = "Pekerjaan selesai"
        }

        binding.status.setOnClickListener {
            startActivity(Intent(requireContext(), DetailJobActivity::class.java))
        }
    }

    companion object {
        fun newInstance() = JobFragment()
        const val ARG_POSITION = "section_number"
        const val ARG_STATUS = "status"
    }

}