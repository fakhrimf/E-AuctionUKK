package com.fakhrimf.e_auctionlatihan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileModel(
    var id: String = "",
    var name: String = "",
    var gender: String = "",
    var email: String = "",
    var role: String = "",
    var origin: String = "",
    var info: String = "",
    var username: String = "",
    var password: String = ""
) : Parcelable