package com.fakhrimf.e_auctionlatihan

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.fakhrimf.e_auctionlatihan.ui.profile.ProfileFragment
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = Repository.getCurrentUser(applicationContext)?.username
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ProfileFragment.newInstance())
                    .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_none, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
