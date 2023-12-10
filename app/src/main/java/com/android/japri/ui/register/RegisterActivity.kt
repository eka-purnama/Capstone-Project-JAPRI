package com.android.japri.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.RequestBody
import com.android.japri.databinding.ActivityRegisterBinding
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val roles = resources.getStringArray(R.array.user_role)
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, roles)
        binding.dropdownRole.setAdapter(adapter)

        binding.btnRegister.setOnClickListener{
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val username = binding.edRegisterUsername.text.toString()
            val role = binding.dropdownRole.text.toString()

            when {
                username.isEmpty() -> binding.edRegisterUsername.error = getString(R.string.empty_input)
                email.isEmpty() -> binding.edRegisterEmail.error = getString(R.string.empty_input)
                password.isEmpty() -> binding.edRegisterPassword.error = getString(R.string.empty_input)
                TextUtils.isEmpty(role) -> binding.roleEditTextLayout.error = getString(R.string.empty_input)
                else -> {
                    binding.roleEditTextLayout.error = null
                    val requestBody = RequestBody.RegisterRequest(email, password, "", username, role)
                    registerAccount(requestBody)
                }
            }
        }

        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.tvInformation.setOnClickListener {
            showInformation()
        }
    }

    private fun registerAccount(registerRequestBody: RequestBody.RegisterRequest){
        registerViewModel.registerAccount(registerRequestBody).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)

                        AlertDialog.Builder(this).apply {
                            setTitle(resources.getString(R.string.register_alert_title))
                            setMessage(resources.getString(R.string.register_alert))
                            setPositiveButton(R.string.text_ok) { _, _ ->
                                val intent = Intent(context, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showInformation(){
        AlertDialog.Builder(this).apply {
            setTitle(R.string.information_alert_title)
            setMessage(R.string.information_alert_content)
            setPositiveButton(R.string.text_ok) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}