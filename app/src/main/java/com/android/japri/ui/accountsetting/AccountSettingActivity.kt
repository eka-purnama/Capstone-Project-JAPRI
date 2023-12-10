package com.android.japri.ui.accountsetting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.android.japri.R
import com.android.japri.databinding.ActivityAccountSettingBinding
import com.android.japri.ui.account.AccountFragment

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_account_setting_activity)

        val gender = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, gender)
        binding.dropdownGender.setAdapter(adapter)

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