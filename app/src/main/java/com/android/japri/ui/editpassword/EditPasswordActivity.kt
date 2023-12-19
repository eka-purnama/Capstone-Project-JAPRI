package com.android.japri.ui.editpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.EditPasswordRequestBody
import com.android.japri.databinding.ActivityEditPasswordBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.showLoading
import com.android.japri.utils.showToast

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPasswordBinding

    private val viewModel by viewModels<EditPasswordViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(R.string.edit_password)

        id = intent.getStringExtra(EXTRA_ID).toString()

        binding.btnEdit.setOnClickListener{
            val password = binding.edNewPassword.text.toString()
            val confirmPassword = binding.edConfirmPassword.text.toString()

            when {
                password.isEmpty() -> binding.passwordEditTextLayout.helperText = getString(R.string.empty_input)
                confirmPassword.isEmpty() -> binding.confirmPasswordEditTextLayout.helperText = getString(R.string.empty_input)
                else -> {
                    val requestBody = EditPasswordRequestBody(password, confirmPassword)
                    editPassword(id, requestBody)
                }
            }
        }
    }

    private fun editPassword(id: String, requestBody: EditPasswordRequestBody){
        viewModel.editPassword(id, requestBody).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressIndicator.showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.progressIndicator.showLoading(false)
                        showToast(result.data.message.toString())
                        finish()
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        binding.progressIndicator.showLoading(false)
                    }
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