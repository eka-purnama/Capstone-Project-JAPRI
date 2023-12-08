package com.android.japri.ui.detailjasa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }
}