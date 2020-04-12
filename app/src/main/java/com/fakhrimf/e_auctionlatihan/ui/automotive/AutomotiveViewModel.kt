package com.fakhrimf.e_auctionlatihan.ui.automotive

import android.app.Application
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel

class AutomotiveViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun getRequestHome() = repo.getRequestHome(context)
}
