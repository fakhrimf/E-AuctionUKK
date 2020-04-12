package com.fakhrimf.e_auctionlatihan.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.RequestItemActivity
import com.fakhrimf.e_auctionlatihan.ui.automotive.AutomotiveFragment
import com.fakhrimf.e_auctionlatihan.utils.ROLE_ADMIN
//import com.fakhrimf.e_auctionlatihan.utils.adapter.SectionPagerAdapterHome
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                    .replace(R.id.fragmentHome, AutomotiveFragment.newInstance())
                    .commitNow()
        }
//        val sectionPagerAdapterHome = SectionPagerAdapterHome(requireContext(), childFragmentManager)
//        val viewPager = viewPagerHome
//        viewPager.adapter = sectionPagerAdapterHome
//        tabLayout.setupWithViewPager(viewPager)
        if (vm.getCurrentUser(requireContext())?.role == ROLE_ADMIN) {
            fabHome.visibility = View.INVISIBLE
        } else {
            fabHome.setOnClickListener {
                startActivity(Intent(context, RequestItemActivity::class.java))
            }
        }
    }
}
