package com.fakhrimf.e_auctionlatihan.ui.reportdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.databinding.ReportDetailFragmentBinding
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.adapter.RecyclerAdapterReport
import kotlinx.android.synthetic.main.report_detail_fragment.*

class ReportDetailFragment(private val itemModel: ItemModel) : Fragment() {

    companion object {
        fun newInstance(itemModel: ItemModel) = ReportDetailFragment(itemModel)
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(ReportDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ReportDetailFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            setVariable(BR.model, this@ReportDetailFragment.itemModel)
            executePendingBindings()
        }
        return binding.root
    }

    private fun setRecycler() {
        vm.getBids(itemModel).observe(viewLifecycleOwner, Observer {
            rvBids.layoutManager = LinearLayoutManager(context)
            rvBids.adapter = RecyclerAdapterReport(it, viewLifecycleOwner)
            progressBarReportDetail.visibility = View.INVISIBLE
            if (it.size < 1) {
                errorNoItem.visibility = View.VISIBLE
                errorNoItemText.visibility = View.VISIBLE
            } else {
                errorNoItem.visibility = View.GONE
                errorNoItemText.visibility = View.GONE
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRecycler()
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                false
            }
            else -> false
        }
    }

}
