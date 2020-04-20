package com.fakhrimf.e_auctionlatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.ui.reportdetail.ReportDetailFragment
import com.fakhrimf.e_auctionlatihan.utils.MODEL_INTENT_KEY

class ReportDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_detail_activity)
        val model = intent.getParcelableExtra<ItemModel>(MODEL_INTENT_KEY)!!
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = model.title
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ReportDetailFragment.newInstance(model))
                    .commitNow()
        }
    }
}
