package com.fakhrimf.e_auctionlatihan.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemModel(
    var id: String = "",
    var title: String = "",
    var status: String = "",
    var user: String = "",
    var desc: String = "",
    var startingPrice: String = "",
    var latestBid: String? = null,
    var highestBidder: String? = null,
    @Exclude
    var myBid: String? = null,
    var image: String = "",
    var due: String = "",
    var dateRequested: String = ""
) : Parcelable