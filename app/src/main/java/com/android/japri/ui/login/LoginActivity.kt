package com.android.japri.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.japri.R
import com.android.japri.databinding.ActivityLoginBinding
import com.android.japri.databinding.ActivityWelcomeScreenBinding
import com.android.japri.ui.main.MainActivity
import com.android.japri.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        binding.tvRegister.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            finish()
        }
    }
}