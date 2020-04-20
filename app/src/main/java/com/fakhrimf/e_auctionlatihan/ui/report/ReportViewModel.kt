package com.fakhrimf.e_auctionlatihan.ui.report

import android.app.Application
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel

class ReportViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun getRequestReport() = repo.getRequestReport(context)
}
