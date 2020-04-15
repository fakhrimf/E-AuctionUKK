package com.fakhrimf.e_auctionlatihan.ui.itemdetail

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.databinding.ItemDetailFragmentBinding
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.DATE_FORMAT
import com.fakhrimf.e_auctionlatihan.utils.NO_BID
import com.fakhrimf.e_auctionlatihan.utils.ROLE_ADMIN
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import com.fakhrimf.e_auctionlatihan.utils.listener.ItemDetailListener
import kotlinx.android.synthetic.main.item_detail_fragment.*
import org.joda.time.DateTime
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ItemDetailFragment(private val model: ItemModel) : BaseFragment(), ItemDetailListener {

    companion object {
        fun newInstance(model: ItemModel) = ItemDetailFragment(model)
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(ItemDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ItemDetailFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            setVariable(BR.model, this@ItemDetailFragment.model)
            listener = this@ItemDetailFragment
            executePendingBindings()
        }
        setHasOptionsMenu(true)
        return binding.root
    }

    private var bid = ""

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val today = DateTime()
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val due = DateTime(sdf.parse(model.due))
        if (vm.getCurrentUser(requireContext())?.role == ROLE_ADMIN || today.isAfter(due) || today.isEqual(due)) {
            bidContainer.visibility = View.GONE
            btnBid.visibility = View.INVISIBLE
        }
        vm.getHighestBids(model).observe(viewLifecycleOwner, Observer {
            loadingBar.visibility = View.INVISIBLE
            bid = it
            if (it != NO_BID) {
                val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
                formatter.currency = Currency.getInstance("IDR")
                latest_bid.text = formatter.format(it.toDouble())
            } else {
                latest_bid.text = getString(R.string.no_bid_yet)
            }
        })
    }

    override fun onSubmit() {
        if (bid != "") {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            formatter.currency = Currency.getInstance("IDR")
            if (bidInput.text.toString() == "") {
                showWarning(getString(R.string.input_your_bid))
            } else {
                if (bidInput.text.toString().toInt() < model.startingPrice.toInt()) {
                    showWarning(getString(R.string.put_price_higher, formatter.format(model.startingPrice.toDouble())))
                } else if (bid != NO_BID && bidInput.text.toString().toInt() < bid.toInt()) {
                    showWarning(getString(R.string.put_price_higher, formatter.format(bid.toDouble())))
                } else {
                    vm.bid(model, bidInput.text.toString()).observe(viewLifecycleOwner, Observer { db ->
                        if (db.success) {
                            showSuccess(getString(R.string.bid_success))
                        } else {
                            showError(db.message)
                        }
                    })
                }
            }
        } else {
            showInfo(getString(R.string.loading))
        }
    }
}
