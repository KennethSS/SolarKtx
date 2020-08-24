package com.solar.solarktx

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun AppCompatImageView.load(url: String?, thumbnail: Float = 0.3f) {
    url?.let {
        Glide.with(this)
            .load(it)
            .thumbnail(thumbnail)
            .into(this)
    }
}