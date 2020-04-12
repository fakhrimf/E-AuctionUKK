package com.fakhrimf.e_auctionlatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.ui.profile.ProfileFragment
import com.fakhrimf.e_auctionlatihan.utils.PROFILE_INTENT_KEY

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_activity)
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.profile)
        val model = intent.getParcelableExtra<ProfileModel>(PROFILE_INTENT_KEY)!!
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ProfileFragment.newInstance(model))
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_none, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
