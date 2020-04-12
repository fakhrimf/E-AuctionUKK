package com.fakhrimf.e_auctionlatihan.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.databinding.RecyclerItemMyBidsBinding
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener

class RecyclerAdapterMyBids(private val list: ArrayList<ItemModel>, private val listener: RecyclerListener) : RecyclerView.Adapter<RecyclerAdapterMyBids.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemMyBidsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.apply {
            listener = this@RecyclerAdapterMyBids.listener
        })
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: RecyclerItemMyBidsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemModel: ItemModel) {
            binding.setVariable(BR.model, itemModel)
            binding.executePendingBindings()
            binding.tvMyBid.text
        }
    }
}