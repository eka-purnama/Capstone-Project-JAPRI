package com.android.japri.ui.accountsetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.RequestBody
import com.android.japri.data.response.UserResponse
import com.android.japri.databinding.ActivityAccountSettingBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.CLIENT
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.EXTRA_ROLE
import com.android.japri.utils.FEMALE
import com.android.japri.utils.MALE

class AccountSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountSettingBinding

    private val accountSettingViewModel by viewModels<AccountSettingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var id: String
    private lateinit var role: String
    private lateinit var userData: UserResponse
    private lateinit var genders: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.title_toolbar_account_setting_activity)

        id = intent.getStringExtra(EXTRA_ID).toString()
        role = intent.getStringExtra(EXTRA_ROLE).toString()
        showUI()

        genders = resources.getStringArray(R.array.gender)
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, genders)
        binding.dropdownGender.setAdapter(adapter)

        getUserById(id)

        binding.btnSave.setOnClickListener{
            editAccount(id)
        }
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

    private fun getUserById(id: String){
        accountSettingViewModel.getUserById(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                    }

                    is ResultState.Success -> {
                        userData = result.data
                        showData(userData)
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showData(userData: UserResponse){
        binding.apply {
            edUsername.setText(userData.username)
            edName.setText(userData.name)
            edEmail.setText(userData.email)
            edPhoneNo.setText(userData.phone_number)
            edAddress.setText(userData.address)
            edDescription.setText(userData.personal_data?.deskripsi)
            dropdownGender.setText(userData.gender,false)
        }
    }

    private fun editAccount(id: String){
        val email = binding.edEmail.text.toString()
        val name = binding.edName.text.toString()
        val phoneNo = binding.edPhoneNo.text.toString()
        val address = binding.edAddress.text.toString()
        val password = binding.edPassword.text.toString()
        val gender = binding.dropdownGender.text.toString()

        val selectedGenderIndex = genders.indexOf(gender)
        val selectedGender = if (selectedGenderIndex == 0) MALE else FEMALE

        val requestBody = RequestBody.AccountRequest(email, name, password, phoneNo, selectedGender, address, null)
        accountSettingViewModel.editAccount(id, requestBody).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                    }
                    is ResultState.Success -> {
                        showToast(result.data.message.toString())
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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