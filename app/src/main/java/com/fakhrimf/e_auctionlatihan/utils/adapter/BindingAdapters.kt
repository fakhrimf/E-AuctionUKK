package com.fakhrimf.e_auctionlatihan.utils.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.utils.DATE_FORMAT
import com.fakhrimf.e_auctionlatihan.utils.GENDER_FEMALE
import com.fakhrimf.e_auctionlatihan.utils.GENDER_MALE
import com.fakhrimf.e_auctionlatihan.utils.repository.Repository
import jp.wasabeef.glide.transformations.BlurTransformation
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("setImageBlurred")
fun setImageBlurred(imageView: ImageView, url: String) {
    Glide.with(imageView.rootView).load(url).apply(RequestOptions.bitmapTransform(BlurTransformation(25))).into(imageView)
}

@BindingAdapter("setImage")
fun setImage(imageView: ImageView, url: String) {
    Glide.with(imageView.rootView).load(url).into(imageView)
}

@BindingAdapter("setGender")
fun setGender(spinner: Spinner, gender: String) {
    when (gender) {
        GENDER_MALE -> spinner.setSelection(0)
        GENDER_FEMALE -> spinner.setSelection(1)
    }
}

@BindingAdapter("setProfileImage")
fun setProfileImage(imageView: ImageView, url: String?) {
    val noImage = "https://vectorified.com/images/no-profile-picture-icon-8.jpg"
    if(url != null) {
        Glide.with(imageView.rootView).load(url).into(imageView)
    } else {
        Glide.with(imageView.rootView).load(noImage).into(imageView)
    }
}

@BindingAdapter("setUserName")
fun setUserName(textView: TextView, user: String) {
    textView.text = Repository.getUserAsModel(user).name
}

@BindingAdapter("setPrice")
fun setPrice(textView: TextView, price: String) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    formatter.currency = Currency.getInstance("IDR")
    textView.text = textView.context.getString(R.string.starting_at, formatter.format(price.toDouble()))
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setRequest")
fun setRequest(textView: TextView, user: String) {
    textView.text = "${textView.context.getString(R.string.requested_by)} ${Repository.getUserAsModel(user).name}"
}

@BindingAdapter("setMyBid")
fun setMyBid(textView: TextView, myBid: String) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
    formatter.currency = Currency.getInstance("IDR")
    textView.text = textView.context.getString(R.string.you_bid, formatter.format(myBid.toDouble()))
}

@BindingAdapter("isWinning")
fun isWinning(constraintLayout: ConstraintLayout, itemModel: ItemModel) {
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    if (itemModel.highestBidder == Repository.getCurrentUser(constraintLayout.context)?.username && Date().before(sdf.parse(itemModel.due))) {
        constraintLayout.visibility = View.VISIBLE
    }
}

@BindingAdapter("isWinner")
fun isWinner(constraintLayout: ConstraintLayout, itemModel: ItemModel) {
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    if (itemModel.highestBidder == Repository.getCurrentUser(constraintLayout.context)?.username && Date().after(sdf.parse(itemModel.due)) || Date() == sdf.parse(itemModel.due)) {
        constraintLayout.visibility = View.VISIBLE
    }
}

@BindingAdapter("isLosing")
fun isLosing(constraintLayout: ConstraintLayout, itemModel: ItemModel) {
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    if (itemModel.highestBidder != Repository.getCurrentUser(constraintLayout.context)?.username && Date().before(sdf.parse(itemModel.due))) {
        constraintLayout.visibility = View.VISIBLE
    }
}

@BindingAdapter("isLost")
fun isLost(constraintLayout: ConstraintLayout, itemModel: ItemModel) {
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
    if (itemModel.highestBidder != Repository.getCurrentUser(constraintLayout.context)?.username && Date().after(sdf.parse(itemModel.due)) || Date() == sdf.parse(itemModel.due)) {
        constraintLayout.visibility = View.VISIBLE
    }
}