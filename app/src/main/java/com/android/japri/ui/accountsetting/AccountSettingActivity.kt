package com.android.japri.ui.accountsetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.android.japri.R
import com.android.japri.databinding.ActivityAccountSettingBinding
import com.android.japri.utils.CLIENT
import com.android.japri.utils.EXTRA_ROLE
import com.android.japri.utils.SERVICE_PROVIDER

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingBinding
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_account_setting_activity)

        role = intent.getStringExtra(EXTRA_ROLE).toString()
        showUI()

        val gender = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, gender)
        binding.dropdownGender.setAdapter(adapter)

    }

    private fun showUI() {
        when (role) {
            CLIENT -> {
                binding.apply {
                    titleSkill.isVisible = false
                    skillEditTextLayout.isVisible = false
                    titleDesc.isVisible = false
                    descEditTextLayout.isVisible = false
                }
            }
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