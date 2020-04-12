package com.fakhrimf.e_auctionlatihan.utils.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.ui.automotive.AutomotiveFragment

class SectionPagerAdapterHome (private val context: Context, fragmentManager: FragmentManager ) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val tabTitles = arrayOf(R.string.auto, R.string.tech, R.string.other)
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AutomotiveFragment.newInstance()
            1 -> AutomotiveFragment.newInstance()
            else -> AutomotiveFragment.newInstance()
        }
    }

    override fun getCount(): Int = tabTitles.size

    override fun getPageTitle(position: Int): CharSequence? = context.getString(tabTitles[position])
}