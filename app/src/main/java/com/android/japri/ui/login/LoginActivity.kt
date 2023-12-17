package com.android.japri.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.android.japri.R
import com.android.japri.data.ResultState
import com.android.japri.data.request.RequestBody
import com.android.japri.databinding.ActivityLoginBinding
import com.android.japri.preferences.UserSessionData
import com.android.japri.ui.ViewModelFactory
import com.android.japri.ui.main.MainActivity
import com.android.japri.ui.register.RegisterActivity
import com.android.japri.utils.showLoading
import com.android.japri.utils.showToast

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener{
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            when {
                email.isEmpty() -> binding.edLoginEmail.error = getString(R.string.empty_input)
                password.isEmpty() -> binding.edLoginPassword.error = getString(R.string.empty_input)
                else -> {
                    val requestBody = RequestBody.LoginRequest(email, password)
                    loginAccount(requestBody)
                }
            }
        }

        binding.tvRegister.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun loginAccount(loginRequestBody: RequestBody.LoginRequest){
        viewModel.login(loginRequestBody).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressBar.showLoading(true)
                    }

                    is ResultState.Success -> {
                        binding.progressBar.showLoading(false)
                        val id = result.data.id.toString()
                        val token = result.data.token.toString()
                        val username = result.data.username.toString()
                        val role = result.data.role.toString()

                        val userSessionData = UserSessionData(id, username, token, role)
                        viewModel.saveSession(userSessionData)

                        showToast(result.data.message.toString())

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }

                    is ResultState.Error -> {
                        showToast(result.error)
                        binding.progressBar.showLoading(false)
                    }
                }
            }
        }
    }
}