package com.fakhrimf.e_auctionlatihan.ui.automotive

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrimf.e_auctionlatihan.*
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.MODEL_INTENT_KEY
import com.fakhrimf.e_auctionlatihan.utils.ROLE_ADMIN
import com.fakhrimf.e_auctionlatihan.utils.adapter.RecyclerAdapter
import com.fakhrimf.e_auctionlatihan.utils.boilerplate.BaseFragment
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener
import kotlinx.android.synthetic.main.automotive_fragment.*

class AutomotiveFragment : BaseFragment(), RecyclerListener {

    companion object {
        fun newInstance() = AutomotiveFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(AutomotiveViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.automotive_fragment, container, false)
    }

    private fun setRecycler() {
        vm.getRequestHome().observe(viewLifecycleOwner, Observer {
            rvAuto.layoutManager = LinearLayoutManager(context)
            rvAuto.adapter = RecyclerAdapter(it, this)
            progressBarHome.visibility = View.INVISIBLE
            srl.isRefreshing = false
            if (it.size < 1) {
                errorNoItem.visibility = View.VISIBLE
                errorNoItemText.visibility = View.VISIBLE
            } else {
                errorNoItem.visibility = View.GONE
                errorNoItemText.visibility = View.GONE
            }
        })
    }

    private fun setRecyclerSearch(query: String) {
        vm.searchHome(query).observe(viewLifecycleOwner, Observer {
            rvAuto.layoutManager = LinearLayoutManager(context)
            rvAuto.adapter = RecyclerAdapter(it, this)
            progressBarHome.visibility = View.INVISIBLE
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
        setHasOptionsMenu(true)
        srl.setOnRefreshListener {
            setRecycler()
        }
        setRecycler()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (vm.getCurrentUser(requireContext())?.role == ROLE_ADMIN) {
            inflater.inflate(R.menu.menu_home_admin_official, menu)
        } else {
            inflater.inflate(R.menu.menu_home, menu)
        }
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                setRecyclerSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                setMenuItemVisibility(menu, false)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                setMenuItemVisibility(menu, true)
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(context, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.itemData -> {
                val intent = Intent(context, ViewRequestActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.report -> {
                val intent = Intent(context, ReportActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    override fun onClick(model: ItemModel) {
        val intent = Intent(context, ItemDetailActivity::class.java)
        intent.putExtra(MODEL_INTENT_KEY, model)
        startActivity(intent)
    }
}
