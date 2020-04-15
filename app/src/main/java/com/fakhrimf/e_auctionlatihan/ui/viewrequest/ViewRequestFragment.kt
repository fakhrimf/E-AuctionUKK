package com.fakhrimf.e_auctionlatihan.ui.viewrequest

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.REQUEST_SHARED_KEY
import com.fakhrimf.e_auctionlatihan.utils.adapter.RecyclerAdapterViewRequest
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.recycler_item.*
import kotlinx.android.synthetic.main.view_request_fragment.*

class ViewRequestFragment : BaseFragment(), RecyclerListener {

    companion object {
        fun newInstance() = ViewRequestFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(ViewRequestViewModel::class.java)
    }

    private lateinit var date: String

    private fun setRecycler() {
        vm.getRequest().observe(viewLifecycleOwner, Observer {
            rvRequest.layoutManager = LinearLayoutManager(context)
            rvRequest.adapter = RecyclerAdapterViewRequest(requireContext(), it, this)
            if(it.size < 1) {
                errorNoItem.visibility = View.VISIBLE
                errorNoItemText.visibility = View.VISIBLE
            }  else {
                errorNoItem.visibility = View.GONE
                errorNoItemText.visibility = View.GONE
            }
        })
    }

    private fun getSelectedItems(): ArrayList<ItemModel> {
        //wut
        val selectedItemShared by lazy {
            Repository.getSharedPreferences(requireContext())?.getString(REQUEST_SHARED_KEY, null)
        }
        return if (selectedItemShared != null) Gson().fromJson(selectedItemShared, object : TypeToken<ArrayList<ItemModel>>() {}.type)
        else ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.view_request_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecycler()
        setHasOptionsMenu(true)
        openDate.setOnClickListener {
            vm.openDatePickerDialog(requireActivity())
        }
        vm.dateLiveData.observe(viewLifecycleOwner, Observer {
            date = it
            dueInput.setText(it)
        })
        btnAccept.setOnClickListener {
            accept()
        }
        btnDecline.setOnClickListener {
            decline()
        }
    }

    private fun accept() {
        if (!isEmpty()) {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE).apply {
                titleText = getString(R.string.accept_ask)
                confirmText = getString(R.string.accept)
                setConfirmClickListener { sweet ->
                    sweet.apply {
                        changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                        titleText = getString(R.string.loading)
                        setCancelable(false)
                    }
                    vm.acceptRequest(getSelectedItems(), date).observe(viewLifecycleOwner, Observer {
                        if (it.success) {
                            sweet.apply {
                                changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                                titleText = getString(R.string.success)
                                getButton(SweetAlertDialog.BUTTON_POSITIVE).visibility = View.GONE
                                val editor = Repository.getSharedPreferences(requireContext())?.edit()
                                editor?.putString(REQUEST_SHARED_KEY, null)
                                editor?.apply()
                            }
                            Handler().postDelayed({
                                sweet.dismissWithAnimation()
                            }, 1000)
                        } else {
                            sweet.apply {
                                changeAlertType(SweetAlertDialog.ERROR_TYPE)
                                titleText = it.message
                            }
                            Handler().postDelayed({
                                sweet.dismissWithAnimation()
                            }, 1000)
                        }
                    })
                }
                show()
            }
        }
    }

    private fun decline() {
        if (!isUnselected()) {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE).apply {
                titleText = getString(R.string.decline_ask)
                confirmText = getString(R.string.decline)
                setConfirmClickListener { sweet ->
                    sweet.apply {
                        changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                        titleText = getString(R.string.loading)
                        setCancelable(false)
                    }
                    vm.declineRequest(getSelectedItems()).observe(viewLifecycleOwner, Observer {
                        if (it.success) {
                            sweet.apply {
                                changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                                titleText = getString(R.string.success)
                                getButton(SweetAlertDialog.BUTTON_POSITIVE).visibility = View.GONE
                                val editor = Repository.getSharedPreferences(requireContext())?.edit()
                                editor?.putString(REQUEST_SHARED_KEY, null)
                                editor?.apply()
                            }
                            Handler().postDelayed({
                                sweet.dismissWithAnimation()
                            }, 1000)
                        } else {
                            sweet.apply {
                                changeAlertType(SweetAlertDialog.ERROR_TYPE)
                                titleText = it.message
                            }
                            Handler().postDelayed({
                                sweet.dismissWithAnimation()
                            }, 1000)
                        }
                    })
                }
                show()
            }
        }
    }

    override fun onClick(model: ItemModel) {
        showInfo(model.title)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_request, menu)
    }

    override fun onDestroy() {
        val editor = Repository.getSharedPreferences(requireContext())?.edit()
        editor?.putString(REQUEST_SHARED_KEY, null)
        editor?.apply()
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                true
            }
            R.id.infoMenu -> {
                showInfo(getString(R.string.info_request))
                true
            }
            else -> true
        }
    }

    private fun isEmpty(): Boolean {
        var check = false
        val selectedItemShared by lazy {
            Repository.getSharedPreferences(requireContext())?.getString(REQUEST_SHARED_KEY, null)
        }
        if (getSelectedItems().size < 1 || selectedItemShared == null || selectedItemShared == "") {
            showWarning(getString(R.string.items_unselected))
            check = true
        } else if (dueInput.text.toString().isBlank()) {
            showWarning(getString(R.string.due_error))
            check = true
        }
        return check
    }

    private fun isUnselected() : Boolean {
        val selectedItemShared by lazy {
            Repository.getSharedPreferences(requireContext())?.getString(REQUEST_SHARED_KEY, null)
        }
        return if (getSelectedItems().size < 1 || selectedItemShared == null || selectedItemShared == "") {
            showWarning(getString(R.string.items_unselected))
            true
        } else {
            false
        }
    }
}
