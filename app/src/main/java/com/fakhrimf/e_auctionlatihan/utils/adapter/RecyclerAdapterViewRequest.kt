package com.fakhrimf.e_auctionlatihan.utils.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhrimf.e_auctionlatihan.BR
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.databinding.RecyclerRequestItemBinding
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.REQUEST_SHARED_KEY
import com.fakhrimf.e_auctionlatihan.utils.listener.RecyclerListener
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository
import com.google.gson.Gson

class RecyclerAdapterViewRequest(private val context: Context, private val list: ArrayList<ItemModel>, private val listener: RecyclerListener) : RecyclerView.Adapter<RecyclerAdapterViewRequest.ViewHolder>() {
    val arrayList = ArrayList<ItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerRequestItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding.apply {
            listener = this@RecyclerAdapterViewRequest.listener
        })
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: RecyclerRequestItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemModel: ItemModel) {
            for (i in arrayList) {
                if (i == itemModel) {
                    binding.checkbox.isSelected = true
                    binding.cardViewCheck.visibility = View.VISIBLE
                    binding.layoutCheck.foreground = context.getDrawable(R.color.colorLogoYellow)
                } else {
                    binding.checkbox.isSelected = false
                    binding.cardViewCheck.visibility = View.INVISIBLE
                    binding.layoutCheck.foreground = context.getDrawable(R.color.colorLogoYellow)
                }
            }
            binding.layoutCheck.setOnClickListener {
                if (binding.checkbox.isSelected) {
                    binding.checkbox.isSelected = false
                    binding.cardViewCheck.visibility = View.INVISIBLE
                    binding.layoutCheck.foreground = context.getDrawable(android.R.color.transparent)
                    itemToArray(itemModel, false)
                } else {
                    binding.checkbox.isSelected = true
                    binding.cardViewCheck.visibility = View.VISIBLE
                    binding.layoutCheck.foreground = context.getDrawable(R.color.colorLogoYellow)
                    itemToArray(itemModel, true)
                }
            }
            binding.setVariable(BR.model, itemModel)
            binding.executePendingBindings()
        }
    }

    fun itemToArray(itemModel: ItemModel, add: Boolean) {
        if (add) arrayList.add(itemModel)
        else arrayList.remove(itemModel)
        val listJson = Gson().toJson(arrayList)
        val editor = Repository.getSharedPreferences(context)?.edit()
        editor?.putString(REQUEST_SHARED_KEY, listJson)
        editor?.apply()
    }
}