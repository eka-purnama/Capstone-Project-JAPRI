package com.android.japri.ui.detailjasa

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.response.UserResponse
import com.android.japri.databinding.ActivityDetailJasaBinding
import com.android.japri.ui.PreferenceViewModel
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.addjob.AddJobActivity
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.EXTRA_USERNAME
import com.android.japri.utils.SERVICE_PROVIDER
import com.android.japri.utils.loadImageOrPlaceholder
import com.android.japri.utils.showToast

class DetailJasaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailJasaBinding

    private var id: String? = null
    private var phoneNumber: String? = null
    private lateinit var role: String
    private lateinit var usernameServiceProvider: String

    private val preferenceViewModel by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val viewModel by viewModels<DetailJasaViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJasaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_detail_jasa_activity)

        id = intent.getStringExtra(EXTRA_ID)
        getUserById(id.toString())

        preferenceViewModel.getSession().observe(this) { user ->
            role = user.role
            showUI()
        }

        binding.btnUseService.setOnClickListener{
            useService()
        }

        binding.btnContact.setOnClickListener {
            openWhatsAppChat(phoneNumber)
        }
    }

    private fun showUI() {
        if (role == SERVICE_PROVIDER) {
            binding.btnContact.isEnabled = false
            binding.btnUseService.isEnabled = false
        }
    }

    private fun getUserById(id: String){
        viewModel.getUserById(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                    }
                    is ResultState.Success -> {
                        showUserData(result.data)
                        usernameServiceProvider = result.data.username.toString()
                        phoneNumber = result.data.phoneNumber
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showUserData(userData: UserResponse){
        binding.apply {
            userPhoto.loadImageOrPlaceholder(userData.photoUrl.toString())
            tvUsername.text = userData.username
            tvName.text = userData.name
            tvName.text = userData.name
            tvAddress.text = userData.address
            tvEmail.text = userData.email
            tvGender.text = userData.gender
            tvUserSkill.text = userData.personalData?.skill?.joinToString(", ")
            tvUserDescription.text = userData.personalData?.deskripsi

            val rating: Float = when (val avgRating: Any? = userData.rating?.averageRating) {
                is Number -> avgRating.toFloat()
                is Boolean -> if (avgRating) 0f else 0f
                else -> 0f
            }
            ratingBar.rating = rating
        }
    }

    private fun useService(){
        AlertDialog.Builder(this).apply {
            setMessage(resources.getString(R.string.has_been_contacted_alert))
            setPositiveButton(R.string.text_yes) { _, _ ->
                val intent = Intent(context, AddJobActivity::class.java)
                intent.putExtra(EXTRA_USERNAME, usernameServiceProvider)
                startActivity(intent)
            }
            setNegativeButton(R.string.text_no) { dialog, _ ->
                dialog.dismiss()
                showToast(getString(R.string.information_for_user))
            }
            create()
            show()
        }
    }

    private fun openWhatsAppChat(phoneNumber: String?) {
        val contact = "+62$phoneNumber"

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$contact")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                getString(R.string.whatsapp_not_installed),
                Toast.LENGTH_SHORT
            ).show()
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