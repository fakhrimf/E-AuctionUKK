package com.fakhrimf.e_auctionlatihan.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.databinding.RecyclerItemBinding
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener

class RecyclerAdapter(private val list: ArrayList<ItemModel>, private val listener: RecyclerListener) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.apply {
            listener = this@RecyclerAdapter.listener
        })
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemModel: ItemModel) {
            binding.setVariable(BR.model, itemModel)
            binding.executePendingBindings()
        }
    }
}