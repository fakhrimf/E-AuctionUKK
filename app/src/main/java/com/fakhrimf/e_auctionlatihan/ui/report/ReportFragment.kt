package com.fakhrimf.e_auctionlatihan.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.ReportDetailActivity
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.MODEL_INTENT_KEY
import com.fakhrimf.e_auctionlatihan.utils.adapter.RecyclerAdapter
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener
import kotlinx.android.synthetic.main.report_fragment.*

class ReportFragment : Fragment(), RecyclerListener {

    companion object {
        fun newInstance() = ReportFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(ReportViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.report_fragment, container, false)
    }

    private fun setRecycler() {
        progressBarReport.visibility = View.VISIBLE
        vm.getRequestReport().observe(viewLifecycleOwner, Observer {
            rvReport.layoutManager = LinearLayoutManager(requireContext())
            rvReport.adapter = RecyclerAdapter(it, this)
            progressBarReport.visibility = View.GONE
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

    override fun onClick(model: ItemModel) {
        val intent = Intent(context, ReportDetailActivity::class.java)
        intent.putExtra(MODEL_INTENT_KEY, model)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                false
            }
            else -> false
        }
    }
}
