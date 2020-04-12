package com.fakhrimf.e_auctionlatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.e_auctionlatihan.ui.viewrequest.ViewRequestFragment

class ViewRequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_request_activity)
        supportActionBar?.elevation = 0F
        supportActionBar?.title = getString(R.string.user_item_request)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ViewRequestFragment.newInstance())
                .commitNow()
        }
    }
}
