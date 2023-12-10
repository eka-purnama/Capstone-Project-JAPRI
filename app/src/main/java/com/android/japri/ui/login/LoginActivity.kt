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

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
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
        loginViewModel.login(loginRequestBody).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        showLoading(false)
                        val token = result.data.token

                        val userSessionData = UserSessionData(123, token.toString(), "Penyedia Jasa")
                        loginViewModel.saveSession(userSessionData)

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
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
}