package com.android.japri.ui.job

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.japri.utils.ARG_POSITION
import com.android.japri.utils.EXTRA_USERNAME

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""
    override fun createFragment(position: Int): Fragment {
        val fragment = JobFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_POSITION, position + 1)
            putString(EXTRA_USERNAME, username)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}