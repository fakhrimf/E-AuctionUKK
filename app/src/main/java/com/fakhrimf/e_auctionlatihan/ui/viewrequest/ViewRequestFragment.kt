package com.fakhrimf.e_auctionlatihan.ui.viewrequest

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.REQUEST_SHARED_KEY
import com.fakhrimf.e_auctionlatihan.utils.adapter.RecyclerAdapterViewRequest
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.view_request_fragment.*

class ViewRequestFragment : BaseFragment(), RecyclerListener {

    companion object {
        fun newInstance() = ViewRequestFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(ViewRequestViewModel::class.java)
    }

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
            dueInput.setText(it)
        })
        btnAccept.setOnClickListener {
            if (!isEmpty()) {
                showInfo(getString(R.string.loading))
                vm.acceptRequest(getSelectedItems(), dueInput.text.toString()).observe(viewLifecycleOwner, Observer {
                    if (it.success) {
                        showSuccess(getString(R.string.items_approved))
                    } else {
                        showError(it.message)
                    }
                })
            }
        }
        btnDecline.setOnClickListener {
            vm.declineRequest(getSelectedItems())
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
}
