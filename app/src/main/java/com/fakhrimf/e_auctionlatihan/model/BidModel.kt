package com.fakhrimf.e_auctionlatihan.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BidModel(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("bid")
    var bid: String = "",
    @SerializedName("bidder")
    var bidder: String = "",
    @SerializedName("bidderId")
    var bidderId: String = ""
) : Parcelable