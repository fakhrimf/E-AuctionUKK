package com.fakhrimf.e_auctionlatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.ui.getadmincode.GetAdminCodeFragment
import com.fakhrimf.e_auctionlatihan.utils.MODEL_INTENT_KEY

class GetAdminCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_admin_code_activity)
        val model = intent.getParcelableExtra<ProfileModel>(MODEL_INTENT_KEY)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, GetAdminCodeFragment.newInstance(model!!))
                    .commitNow()
        }
    }
}
