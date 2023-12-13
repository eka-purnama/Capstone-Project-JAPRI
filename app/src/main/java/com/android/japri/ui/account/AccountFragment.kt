package com.android.japri.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.japri.R
import com.android.japri.databinding.FragmentAccountBinding
import com.android.japri.ui.ViewModelFactoryWithId
import com.android.japri.ui.aboutapp.AboutAppActivity
import com.android.japri.ui.accountsetting.AccountSettingActivity
import com.android.japri.ui.jobhistory.JobHistoryActivity
import com.android.japri.ui.photoprofile.PhotoProfileActivity
import com.android.japri.ui.welcome.WelcomeScreenActivity
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.EXTRA_PHOTO_URL
import com.android.japri.utils.EXTRA_ROLE
import com.android.japri.utils.SERVICE_PROVIDER
import com.android.japri.utils.loadImage

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null

    private val binding get() = _binding!!

    private lateinit var accountViewModel: AccountViewModel

    private var userId: String? = null
    private var role: String? = null
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

        userId = arguments?.getString(EXTRA_ID)
        role = arguments?.getString(EXTRA_ROLE)

        val factory = ViewModelFactoryWithId.getInstance(requireContext(), userId.toString())
        accountViewModel = ViewModelProvider(this, factory)[AccountViewModel::class.java]

        accountViewModel.user.observe(viewLifecycleOwner) { userData ->
            binding.userPhoto.loadImage(userData.photo_url)
            binding.tvAccountUsername.text = userData.username
            photoUrl = userData.photo_url.toString()
        }

        showUI()
        getLoading()

        binding.editPhoto.setOnClickListener {
            val intentPhotoProfile= Intent(requireContext(), PhotoProfileActivity::class.java)
            intentPhotoProfile.putExtra(EXTRA_ID, userId)
            intentPhotoProfile.putExtra(EXTRA_PHOTO_URL, photoUrl)
            startActivity(intentPhotoProfile)
        }
        binding.historyJobCardView.setOnClickListener{
            val intentHistory = Intent(requireContext(), JobHistoryActivity::class.java)
            intentHistory.putExtra(EXTRA_ROLE, role)
            startActivity(intentHistory)
        }
        binding.accountSetting.setOnClickListener {
            val intentAccountSetting = Intent(requireContext(), AccountSettingActivity::class.java)
            intentAccountSetting.putExtra(EXTRA_ROLE, role)
            startActivity(intentAccountSetting)
        }
        binding.aboutApp.setOnClickListener {
            startActivity(Intent(requireContext(), AboutAppActivity::class.java))
        }
        binding.btnLogout.setOnClickListener{
            showAlertDialog()
        }
    }

    private fun showUI() {
        binding.historyType.text = (
            if (role == SERVICE_PROVIDER) resources.getString(R.string.history_job)
            else resources.getString(R.string.history_service)
        )
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

    private fun getLoading(){
        accountViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}