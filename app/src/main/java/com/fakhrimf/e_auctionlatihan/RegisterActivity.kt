package com.fakhrimf.e_auctionlatihan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.e_auctionlatihan.ui.register.RegisterFragment

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RegisterFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        this.finish()
    }
}
