package com.fakhrimf.e_auctionlatihan.ui.itemdetail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.fakhrimf.e_auctionlatihan.model.BidModel
import com.fakhrimf.e_auctionlatihan.model.DatabaseMessageModel
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository

class ItemDetailViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun getHighestBids(itemModel: ItemModel) = repo.getHighestBid(itemModel)
    fun bid(itemModel: ItemModel, bid: String): MutableLiveData<DatabaseMessageModel> {
        val bidModel = BidModel(bid = bid, bidder = "${Repository.getCurrentUser(context)?.username}", bidderId = "${Repository.getCurrentUser(context)?.id}")
        return repo.bid(context, itemModel, bidModel)
    }
}
