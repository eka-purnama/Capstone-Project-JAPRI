package com.android.japri.ui.job

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.japri.databinding.FragmentJobBinding
import com.android.japri.ui.detailjob.DetailJobActivity

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_STATUS)
        }

//        if (position == 1){
//            binding.status.text = "Proses"
//        } else {
//            binding.status.text = "Selesai"
//        }

//        binding.status.setOnClickListener {
//            startActivity(Intent(requireContext(), DetailJobActivity::class.java))
//        }
    }

    companion object {
        fun newInstance() = JobFragment()
        const val ARG_POSITION = "section_number"
        const val ARG_STATUS = "status"
    }

}