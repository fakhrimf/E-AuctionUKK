package com.fakhrimf.e_auctionlatihan.ui.reportdetail

import android.app.Application
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel

class ReportDetailViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun getBids(itemModel: ItemModel) = repo.getBids(itemModel)
}
