package com.android.japri.ui.detailjasa

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.android.japri.R
import com.android.japri.databinding.ActivityDetailJasaBinding
import com.android.japri.ui.PreferenceViewModel
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.addjob.AddJobActivity
import com.android.japri.utils.SERVICE_PROVIDER
import com.android.japri.utils.showToast

class DetailJasaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailJasaBinding

    private val preferenceViewModel by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJasaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_detail_jasa_activity)

        preferenceViewModel.getSession().observe(this) { user ->
            role = user.role
            showUI()
        }

        binding.btnUseService.setOnClickListener{
            AlertDialog.Builder(this).apply {
                setMessage(resources.getString(R.string.has_been_contacted_alert))
                setPositiveButton(R.string.text_yes) { _, _ ->
                    startActivity(Intent(context, AddJobActivity::class.java))
                }
                setNegativeButton(R.string.text_no) { dialog, _ ->
                    dialog.dismiss()
                    showToast(getString(R.string.information_for_user))
                }
                create()
                show()
            }
        }

        binding.btnContact.setOnClickListener {
            openWhatsAppChat("81234567890")
        }
    }

    private fun showUI() {
        when (role) {
            SERVICE_PROVIDER -> {
                binding.btnContact.isEnabled = false
                binding.btnUseService.isEnabled = false
            }
        }
    }

    private fun openWhatsAppChat(phoneNumber: String) {
        val contact = "+62$phoneNumber" // Tambahkan tanda "+" sebelum nomor

        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$contact")
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "WhatsApp tidak terpasang.", Toast.LENGTH_SHORT).show()
        }
    }
}