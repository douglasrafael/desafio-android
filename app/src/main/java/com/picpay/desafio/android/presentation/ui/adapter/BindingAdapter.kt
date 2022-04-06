package com.picpay.desafio.android.presentation.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.picpay.desafio.android.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

@BindingAdapter(value = ["imgUrl", "progressBar"], requireAll = true)
fun loadImg(view: ImageView, imgUrl: String?, progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
    Picasso.get()
        .load(imgUrl)
        .error(R.drawable.ic_round_account_circle)
        .into(view, object : Callback {
            override fun onSuccess() {
                progressBar.visibility = View.GONE
            }

            override fun onError(e: Exception?) {
                progressBar.visibility = View.GONE
            }
        })
}