package com.fakhrimf.e_auctionlatihan.ui.requestitem

import android.app.Activity
import android.app.Application
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel

class RequestItemViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun showWarningAlert(activity: Activity, confirmListener: SweetAlertDialog.OnSweetClickListener, itemPrice: String) {
        SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE).apply{
            titleText = context.getString(R.string.ask_sure)
            contentText = context.getString(R.string.request_for_auction, itemPrice)
            confirmText = context.getString(R.string.positive_request)
            setConfirmClickListener(confirmListener)
            showCancelButton(true)
            setCancelable(true)
            show()
        }
    }
}
