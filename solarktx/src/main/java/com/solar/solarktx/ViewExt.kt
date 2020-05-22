package com.solar.solarktx

import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(msg: String, length: Int = Snackbar.LENGTH_SHORT) {
    val snack = Snackbar.make(this, msg, length)
    val view = snack.view
    val tv = view.findViewById(R.id.snackbar_text) as TextView
    tv.gravity = Gravity.CENTER_HORIZONTAL
    snack.show()
}

fun View.toast(msg: Any, length: Int = Toast.LENGTH_SHORT) {
    context.toast(msg, length)
}