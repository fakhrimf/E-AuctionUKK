package com.fakhrimf.e_auctionlatihan.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.databinding.RecyclerItemBidsBinding
import com.fakhrimf.e_auctionlatihan.model.BidModel
import com.fakhrimf.e_auctionlatihan.utils.NO_IMAGE
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository

class RecyclerAdapterReport(private val list: ArrayList<BidModel>, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<RecyclerAdapterReport.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemBidsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    private fun setImage(imageView: ImageView, bidModel: BidModel) {
        val repo = Repository.newInstance()
        repo.getProfileData(bidModel.bidderId).observe(lifecycleOwner, Observer {
            val url = it.image
            if(url != null) {
                Glide.with(imageView.rootView).load(url).into(imageView)
            } else {
                Glide.with(imageView.rootView).load(NO_IMAGE).into(imageView)
            }
        })
    }

    inner class ViewHolder(private val binding: RecyclerItemBidsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bidModel: BidModel) {
            binding.setVariable(BR.model, bidModel)
            setImage(binding.imageProfile, bidModel)
            binding.executePendingBindings()
        }
    }
}