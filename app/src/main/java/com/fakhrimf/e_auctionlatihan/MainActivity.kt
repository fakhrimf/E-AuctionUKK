package com.fakhrimf.e_auctionlatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fakhrimf.e_auctionlatihan.ui.splash.SplashFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SweetAlertDialog.DARK_STYLE = false
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mSplashScreenFragment, SplashFragment.newInstance())
                .commitNow()
        }
    }
}
