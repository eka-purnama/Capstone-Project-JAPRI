package com.android.japri.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.android.japri.R
import com.android.japri.databinding.ActivityRegisterBinding
import com.android.japri.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnRegister.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }

        binding.tvInformation.setOnClickListener {
            showInformation()
        }

        val roles = resources.getStringArray(R.array.user_role)
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, roles)
        binding.dropdownRole.setAdapter(adapter)

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