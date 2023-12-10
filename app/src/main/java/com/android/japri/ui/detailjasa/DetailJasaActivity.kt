package com.android.japri.ui.detailjasa

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.japri.R
import com.android.japri.databinding.ActivityDetailJasaBinding
import com.android.japri.ui.addjob.AddJobActivity

class DetailJasaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailJasaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJasaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_detail_jasa_activity)

        binding.btnUseService.setOnClickListener{
            startActivity(Intent(this, AddJobActivity::class.java))
        }

        binding.btnContact.setOnClickListener {
            openWhatsAppChat("81234567890")
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