package com.fakhrimf.e_auctionlatihan.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fakhrimf.e_auctionlatihan.HomeActivity
import com.fakhrimf.e_auctionlatihan.LoginActivity
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.utils.SPLASH_DELAY

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent(requireContext(), LoginActivity::class.java)
        val handler = Handler()
        handler.postDelayed({
            startActivity(intent)
            requireActivity().finish()
        }, SPLASH_DELAY)
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}
