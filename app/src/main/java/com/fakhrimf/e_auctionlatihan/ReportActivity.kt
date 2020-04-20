package com.fakhrimf.e_auctionlatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.e_auctionlatihan.ui.report.ReportFragment

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.report)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ReportFragment.newInstance())
                    .commitNow()
        }
    }
}
