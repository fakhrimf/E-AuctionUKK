package com.fakhrimf.e_auctionlatihan.ui.profile

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel

class ProfileViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun getMyBids(lifecycleOwner: LifecycleOwner) = repo.getMyBids(lifecycleOwner, context)
}
