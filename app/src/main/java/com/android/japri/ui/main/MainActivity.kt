package com.android.japri.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.japri.R
import com.android.japri.databinding.ActivityMainBinding
import com.android.japri.ui.PreferenceViewModel
import com.android.japri.ui.ViewModelFactory
import com.android.japri.utils.EXTRA_ID
import com.android.japri.utils.EXTRA_ROLE
import com.android.japri.utils.EXTRA_USERNAME

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val preferenceViewModel by viewModels<PreferenceViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var role: String
    private lateinit var userId: String
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceViewModel.getSession().observe(this) { user ->
            role = user.role
            userId = user.id
            username = user.username
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_jasa, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_dashboard -> {
                    navigateToDashboardFragment()
                    true
                }
                R.id.navigation_jasa -> {
                    navigateToJasaFragment()
                    true
                }
                R.id.navigation_account -> {
                    navigateToAccountFragment()
                    true
                }
                else -> false
            }
        }
    }

    private fun navigateToDashboardFragment() {
        findNavController(R.id.nav_host_fragment_activity_main)
            .navigate(R.id.navigation_dashboard)
    }

    private fun navigateToJasaFragment() {
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)
        findNavController(R.id.nav_host_fragment_activity_main)
            .navigate(R.id.navigation_jasa, bundle)
    }

    private fun navigateToAccountFragment() {
        val bundle = Bundle()
        bundle.putString(EXTRA_ID, userId)
        bundle.putString(EXTRA_ROLE, role)
        findNavController(R.id.nav_host_fragment_activity_main)
            .navigate(R.id.navigation_account, bundle)
    }
}