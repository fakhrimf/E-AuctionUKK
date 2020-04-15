package com.fakhrimf.e_auctionlatihan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.EditProfileActivity
import com.fakhrimf.e_auctionlatihan.ItemDetailActivity
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.databinding.ProfileFragmentBinding
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.MODEL_INTENT_KEY
import com.fakhrimf.e_auctionlatihan.utils.PROFILE_INTENT_KEY
import com.fakhrimf.e_auctionlatihan.utils.ROLE_ADMIN
import com.fakhrimf.e_auctionlatihan.utils.adapter.RecyclerAdapterMyBids
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment(), RecyclerListener {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val vm by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(ProfileViewModel::class.java)
    }

    private val model by lazy {
        Repository.getCurrentUser(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = ProfileFragmentBinding.inflate(inflater, container, false)
        binding.apply {
            setVariable(BR.model, this@ProfileFragment.model)
            executePendingBindings()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (vm.getCurrentUser(requireContext())?.role != ROLE_ADMIN) {
            vm.getMyBids(viewLifecycleOwner).observe(viewLifecycleOwner, Observer {
                rvMyBids.layoutManager = LinearLayoutManager(requireContext())
                rvMyBids.adapter = RecyclerAdapterMyBids(it, this)
                if (it.size < 1) {
                    adminTitle.visibility = View.VISIBLE
                    noItem.visibility = View.VISIBLE
                    adminTitle.text = getString(R.string.no_bid)
                } else {
                    adminTitle.visibility = View.GONE
                    noItem.visibility = View.GONE
                }
            })
        } else {
            adminTitle.visibility = View.VISIBLE
            rvMyBids.visibility = View.GONE
            myBidTitle.visibility = View.GONE
        }
//        val viewPager = viewPagerProfile
//        viewPager.adapter = SectionPagerAdapterProfile(requireContext(), childFragmentManager)
        setHasOptionsMenu(true)
//        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressed()
                false
            }
            R.id.editProfile -> {
                val intent = Intent(context, EditProfileActivity::class.java)
                intent.putExtra(PROFILE_INTENT_KEY, model)
                startActivity(intent)
                requireActivity().finish()
                false
            }
            else -> false
        }
    }

    override fun onClick(model: ItemModel) {
        val intent = Intent(context, ItemDetailActivity::class.java)
        intent.putExtra(MODEL_INTENT_KEY, model)
        startActivity(intent)
    }
}
