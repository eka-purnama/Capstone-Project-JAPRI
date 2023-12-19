package com.android.japri.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.databinding.FragmentAccountBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.aboutapp.AboutAppActivity
import com.android.japri.ui.accountsetting.AccountSettingActivity
import com.android.japri.ui.editpassword.EditPasswordActivity
import com.android.japri.ui.jobhistory.JobHistoryActivity
import com.android.japri.ui.photoprofile.PhotoProfileActivity
import com.android.japri.ui.welcome.WelcomeScreenActivity
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.EXTRA_PHOTO_URL
import com.android.japri.utils.EXTRA_ROLE
import com.android.japri.utils.EXTRA_USERNAME
import com.android.japri.utils.SERVICE_PROVIDER
import com.android.japri.utils.loadImage
import com.android.japri.utils.showLoading

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    private val accountViewModel by viewModels<AccountViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var userId: String? = null
    private var role: String? = null
    private lateinit var username: String
    private lateinit var photoUrl: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        arguments?.let {
            userId = it.getString(EXTRA_ID)
            role = it.getString(EXTRA_ROLE)
        }

        getUserById(userId.toString())

        showUI()

        binding.editPhoto.setOnClickListener {
            val intentPhotoProfile= Intent(requireContext(), PhotoProfileActivity::class.java)
            intentPhotoProfile.putExtra(EXTRA_ID, userId)
            intentPhotoProfile.putExtra(EXTRA_PHOTO_URL, photoUrl)
            startActivity(intentPhotoProfile)
        }
        binding.historyJobCardView.setOnClickListener{
            val intentHistory = Intent(requireContext(), JobHistoryActivity::class.java)
            intentHistory.putExtra(EXTRA_ROLE, role)
            intentHistory.putExtra(EXTRA_USERNAME, username)
            startActivity(intentHistory)
        }
        binding.accountSetting.setOnClickListener {
            val intentAccountSetting = Intent(requireContext(), AccountSettingActivity::class.java)
            intentAccountSetting.putExtra(EXTRA_ID, userId)
            intentAccountSetting.putExtra(EXTRA_ROLE, role)
            startActivity(intentAccountSetting)
        }
        binding.editPassword.setOnClickListener {
            val intentAccountSetting = Intent(requireContext(), EditPasswordActivity::class.java)
            intentAccountSetting.putExtra(EXTRA_ID, userId)
            startActivity(intentAccountSetting)
        }
        binding.aboutApp.setOnClickListener {
            startActivity(Intent(requireContext(), AboutAppActivity::class.java))
        }
        binding.btnLogout.setOnClickListener{
            showAlertDialog()
        }
    }

    private fun getUserById(id: String){
        accountViewModel.getUserById(id).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.showLoading(true)                    }

                    is ResultState.Success -> {
                        binding.progressBar.showLoading(false)
                        binding.tvAccountUsername.text = result.data.username
                        username = result.data.username.toString()

                        photoUrl = result.data.photoUrl.toString()
                        if (photoUrl.isEmpty()){
                            binding.userPhoto.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.user_photo))
                        } else {
                            binding.userPhoto.loadImage(photoUrl)
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

    private fun showUI() {
        if (role == SERVICE_PROVIDER) {
            binding.historyType.text = resources.getString(R.string.history_job)
            binding.title.text = resources.getString(R.string.account_service_provider)
        }
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.logout)
            setMessage(R.string.logout_message)
            setPositiveButton(R.string.text_yes) { _, _ ->
                accountViewModel.logout()
                startActivity(Intent(requireActivity(), WelcomeScreenActivity::class.java))
                requireActivity().finish()
            }
            setNegativeButton(R.string.text_no) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        userId?.let {
            getUserById(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}