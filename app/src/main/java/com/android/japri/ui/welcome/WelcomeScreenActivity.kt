package com.android.japri.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.japri.databinding.ActivityWelcomeScreenBinding
import com.android.japri.ui.login.LoginActivity
import com.android.japri.ui.register.RegisterActivity

class WelcomeScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWelcomeScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener{
            startActivity(Intent(this@WelcomeScreenActivity, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this@WelcomeScreenActivity, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.welcomePicture, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA,1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.description, View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, desc, login, register)
            start()
        }
    }
}