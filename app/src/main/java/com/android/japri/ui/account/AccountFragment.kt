package com.android.japri.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.japri.databinding.FragmentAccountBinding
import com.android.japri.ui.aboutapp.AboutAppActivity
import com.android.japri.ui.accountsetting.AccountSettingActivity
import com.android.japri.ui.jobhistory.JobHistoryActivity
import com.android.japri.ui.photoprofile.PhotoProfileActivity

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val accountViewModel = ViewModelProvider(this)[AccountViewModel::class.java]

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changePhoto.setOnClickListener {
            startActivity(Intent(requireContext(), PhotoProfileActivity::class.java))
        }
        binding.historyJobCardView.setOnClickListener{
            startActivity(Intent(requireContext(), JobHistoryActivity::class.java))
        }
        binding.accountSetting.setOnClickListener {
            startActivity(Intent(requireContext(), AccountSettingActivity::class.java))
        }
        binding.aboutApp.setOnClickListener {
            startActivity(Intent(requireContext(), AboutAppActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}