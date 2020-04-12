package com.fakhrimf.e_auctionlatihan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.ui.itemdetail.ItemDetailFragment
import com.fakhrimf.e_auctionlatihan.utils.MODEL_INTENT_KEY

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail_activity)
        val model = intent.getParcelableExtra<ItemModel>(MODEL_INTENT_KEY)!!
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = model.title
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ItemDetailFragment.newInstance(model))
                .commitNow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }
}
