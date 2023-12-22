package com.android.japri.ui.jobhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.viewpager2.widget.ViewPager2
import com.android.japri.R
import com.android.japri.databinding.ActivityJobHistoryBinding
import com.android.japri.ui.job.SectionsPagerAdapter
import com.android.japri.utils.EXTRA_ROLE
import com.android.japri.utils.EXTRA_USERNAME
import com.android.japri.utils.SERVICE_PROVIDER
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class JobHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobHistoryBinding
    private lateinit var role: String
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        role = intent.getStringExtra(EXTRA_ROLE).toString()
        username = intent.getStringExtra(EXTRA_USERNAME)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (role) {
            SERVICE_PROVIDER -> supportActionBar?.setTitle(R.string.title_toolbar_job_history_activity)
            else -> supportActionBar?.setTitle(R.string.title_toolbar_service_history_activity)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}