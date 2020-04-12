package com.fakhrimf.e_auctionlatihan.utils.boilerplate

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.IMAGE_TYPE
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository

open class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {
    val repo = Repository.newInstance()
    protected val context by lazy {
        getApplication() as Context
    }

    fun login(context: Context, lifecycleOwner: LifecycleOwner, profileModel: ProfileModel) = repo.login(context, lifecycleOwner, profileModel)
    fun register(context: Context, profileModel: ProfileModel) = repo.registerProfile(context, profileModel)
    fun getCurrentUser(context: Context) = Repository.getCurrentUser(context)
    fun getUserAsString(profileModel: ProfileModel) = Repository.getUserAsString(profileModel)
    fun addRequest(itemModel: ItemModel, path: Uri) = repo.addRequest(itemModel, path)
    fun getAdminCode() = repo.getAdminCode()
    fun getApprovedRequest() = repo.getApprovedRequest(context)
    fun getImageIntent(): Intent {
        val intent = Intent()
        intent.apply {
            type = IMAGE_TYPE
            action = Intent.ACTION_GET_CONTENT
        }
        return Intent.createChooser(intent, context.getString(R.string.select_image))
    }

    fun getImageBitmap(contentResolver: ContentResolver, path: Uri): Bitmap {
        @Suppress("DEPRECATION") return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, path))
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, path)
        }
    }
}