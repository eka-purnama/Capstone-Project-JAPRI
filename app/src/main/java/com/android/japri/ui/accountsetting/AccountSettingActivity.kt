package com.android.japri.ui.accountsetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.android.japri.R
import com.android.japri.databinding.ActivityAccountSettingBinding
import com.android.japri.databinding.ActivityDetailJasaBinding

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_account_setting_activity)

        val gender = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, gender)
        binding.dropdownGender.setAdapter(adapter)
    }
}