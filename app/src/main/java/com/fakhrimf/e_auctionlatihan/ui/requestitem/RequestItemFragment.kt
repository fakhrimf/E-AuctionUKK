package com.fakhrimf.e_auctionlatihan.ui.requestitem

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.IMAGE_REQUEST_CODE
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import kotlinx.android.synthetic.main.request_item_fragment.*
import java.text.NumberFormat
import java.util.*

class RequestItemFragment : BaseFragment() {

    companion object {
        fun newInstance() = RequestItemFragment()
    }

    private var path: Uri? = null
    private var bitmap: Bitmap? = null

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(RequestItemViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.request_item_fragment, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && data != null && data.data != null) {
            path = data.data!!
            bitmap = vm.getImageBitmap(requireActivity().contentResolver, path!!)
            itemImage.setImageBitmap(bitmap)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnAddPic.setOnClickListener {
            startActivityForResult(vm.getImageIntent(), IMAGE_REQUEST_CODE)
        }
        btnRequest.setOnClickListener {
            addRequest()
        }
    }

    private fun addRequest() {
        if (!isEmpty()) {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = Currency.getInstance("IDR")
            vm.showWarningAlert(
                confirmListener = SweetAlertDialog.OnSweetClickListener { sweet ->
                    progressBarRequest.visibility = View.VISIBLE
                    sweet.apply {
                        changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                        titleText = getString(R.string.loading)
                        contentText = getString(R.string.uploading_request)
                        showCancelButton(false)
                        setCancelable(false)
                    }
                    val user = vm.getCurrentUser(requireContext())!!
                    user.password = ""
                    user.email = ""
                    val model = ItemModel(
                        title = nameInput.text.toString(),
                        desc = descInput.text.toString(),
                        startingPrice = priceInput.text.toString(),
                        user = vm.getUserAsString(user)
                    )
                    path?.let {
                        vm.addRequest(model, it).observe(viewLifecycleOwner, Observer { db ->
                            if (db.success) {
                                progressBarRequest.visibility = View.INVISIBLE
                                sweet.apply {
                                    changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                                    titleText = getString(R.string.success)
                                    contentText = getString(R.string.request_success)
                                    confirmText = getString(R.string.ok)
                                    setCancelable(false)
                                    setConfirmClickListener {
                                        requireActivity().finish()
                                    }
                                }
                            } else {
                                showError(db.message)
                            }
                        })
                    }
                },
                itemPrice = formatter.format(priceInput.text.toString().toDouble()),
                activity = requireActivity())
        }
    }

    private fun isEmpty(): Boolean {
        var check = false
        if (nameInput.text.toString().isBlank()) {
            check = true
            nameInput.error = getString(R.string.title_error)
        }
        if (descInput.text.toString().isBlank()) {
            check = true
            descInput.error = getString(R.string.desc_error)
        }
        if (priceInput.text.toString().isBlank()) {
            check = true
            priceInput.error = getString(R.string.price_error)
        }
        if (path == null) {
            check = true
            showWarning(getString(R.string.image_error))
        }
        return check
    }
}
