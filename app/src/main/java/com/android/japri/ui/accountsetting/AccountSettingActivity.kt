package com.android.japri.ui.accountsetting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.AccountRequestBody
import com.android.japri.data.request.PersonalDataRequest
import com.android.japri.data.response.UserResponse
import com.android.japri.databinding.ActivityAccountSettingBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.CLIENT
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.EXTRA_ROLE
import com.android.japri.utils.FEMALE
import com.android.japri.utils.MALE
import com.android.japri.utils.SkillDialogFragment
import com.android.japri.utils.showToast

class AccountSettingActivity : AppCompatActivity(), SkillDialogFragment.OnOptionDialogListener {

    private lateinit var binding: ActivityAccountSettingBinding

    private val viewModel by viewModels<AccountSettingViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var id: String
    private lateinit var role: String
    private lateinit var userSkill: Array<String>
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

        binding.edSkill.setOnClickListener{
            val skillDialogFragment = SkillDialogFragment()
            skillDialogFragment.show(supportFragmentManager, SkillDialogFragment::class.java.simpleName)
        }

        binding.btnSave.setOnClickListener{
            editAccount()
        }
    }

    private fun editAccount(){
        binding.apply {
            val email = edEmail.text.toString()
            val name = edName.text.toString()
            val phoneNo = edPhoneNo.text.toString()
            val address = edAddress.text.toString()
            val gender = dropdownGender.text.toString()
            val skills = edSkill.text.toString()
            val description = edDescription.text.toString()

            when {
                name.isEmpty() -> edName.error = getString(R.string.empty_input)
                email.isEmpty() -> edEmail.error = getString(R.string.empty_input)
                phoneNo.isEmpty() -> edPhoneNo.error = getString(R.string.empty_input)
                TextUtils.isEmpty(gender) -> genderEditTextLayout.error = getString(R.string.empty_input)
                address.isEmpty() -> edAddress.error = getString(R.string.empty_input)
                else -> {
                    val selectedGenderIndex = genders.indexOf(gender)
                    val selectedGender = if (selectedGenderIndex == 0) MALE else FEMALE

                    if (role == CLIENT) {
                        val requestBody = AccountRequestBody(
                            email, name, phoneNo, selectedGender, address, null
                        )
                        saveChanges(id, requestBody)
                    } else {
                        when {
                            skills.isEmpty() -> skillEditTextLayout.error = getString(R.string.empty_input)
                            description.isEmpty() -> edDescription.error = getString(R.string.empty_input)
                            else -> {
                                val personalData = PersonalDataRequest(userSkill, description)
                                val requestBody = AccountRequestBody(
                                    email, name, phoneNo, selectedGender, address, personalData
                                )
                                saveChanges(id, requestBody)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveChanges(id: String, requestBody: AccountRequestBody){
        viewModel.editAccount(id, requestBody).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                    }
                    is ResultState.Success -> {
                        showToast(result.data.message.toString())
                        finish()
                    }
                    is ResultState.Error -> {
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun showUI() {
        if (role == CLIENT) {
            binding.apply {
                titleSkill.isVisible = false
                skillEditTextLayout.isVisible = false
                titleDesc.isVisible = false
                descEditTextLayout.isVisible = false
            }
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
            edUsername.setText(userData.username)
            edName.setText(userData.name)
            edEmail.setText(userData.email)
            edPhoneNo.setText(userData.phoneNumber)
            edAddress.setText(userData.address)
            dropdownGender.setText(userData.gender,false)
            edDescription.setText(userData.personalData?.deskripsi)
            userSkill = userData.personalData?.skill?.mapNotNull { it }?.toTypedArray() ?: emptyArray()

            val combinedSkills: String? = userData.personalData?.skill?.joinToString(", ")
            edSkill.setText(combinedSkills)
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

    override fun onOptionChosen(skills: Array<String>?) {
        if (skills != null) {
            userSkill = skills
            val combinedSkills: String = skills.joinToString(", ")
            binding.edSkill.setText(combinedSkills)
        }
    }
}