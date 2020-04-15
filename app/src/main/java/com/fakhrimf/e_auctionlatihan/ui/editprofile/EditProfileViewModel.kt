package com.fakhrimf.e_auctionlatihan.ui.editprofile

import android.app.Application
import android.net.Uri
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel

class EditProfileViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun editProfile(profileModel: ProfileModel, path: Uri?) = repo.editProfile(context, profileModel, path)
}
