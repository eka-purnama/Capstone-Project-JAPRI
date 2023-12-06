package com.android.japri.ui.job

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var status: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragment = JobFragment()
        fragment.arguments = Bundle().apply {
            putInt(JobFragment.ARG_POSITION, position + 1)
            putString(JobFragment.ARG_STATUS, status)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}