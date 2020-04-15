package com.fakhrimf.e_auctionlatihan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.ui.editprofile.EditProfileFragment
import com.fakhrimf.e_auctionlatihan.utils.PROFILE_INTENT_KEY

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
        supportActionBar?.title = getString(R.string.edit_profile)
        val model = intent.getParcelableExtra<ProfileModel>(PROFILE_INTENT_KEY)!!
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, EditProfileFragment.newInstance(model))
                    .commitNow()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, ProfileActivity::class.java))
        this.finish()
    }
}
