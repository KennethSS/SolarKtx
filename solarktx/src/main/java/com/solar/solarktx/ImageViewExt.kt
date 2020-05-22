package com.solar.solarktx

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide

fun AppCompatImageView.load(url: String?) {
    url?.let {
        Glide.with(this)
            .load(it)
            .into(this)
    }
}