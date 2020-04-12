package com.fakhrimf.e_auctionlatihan.ui.viewrequest

import android.app.Activity
import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.DATE_FORMAT
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseAndroidViewModel
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class ViewRequestViewModel(application: Application) : BaseAndroidViewModel(application) {
    fun getRequest() = repo.getPendingRequest(context)
    fun acceptRequest(items: ArrayList<ItemModel>, due: String) = repo.acceptRequest(items, due)
    fun declineRequest(items: ArrayList<ItemModel>) = repo.declineRequest(items)
    val dateLiveData by lazy {
        MutableLiveData<String>()
    }

    private var dateTime = DateTime()

    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
        dateTime = dateTime.withYear(year)
        dateTime = dateTime.withMonthOfYear(month)
        dateTime = dateTime.withDayOfMonth(dayOfMonth)
        dateLiveData.value = DateTimeFormat.forPattern(DATE_FORMAT).print(dateTime)
    }

    fun openDatePickerDialog(activity: Activity) {
        val datePickerDialog = DatePickerDialog(activity, dateSetListener, dateTime.year, dateTime.monthOfYear, dateTime.dayOfMonth)
        datePickerDialog.datePicker.minDate = DateTime().millis - 100
        datePickerDialog.datePicker.maxDate = DateTime().plusMonths(3).millis
        datePickerDialog.show()
    }
}
