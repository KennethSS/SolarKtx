package com.solar.solarktx

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.MainThread
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

fun View.toBitmap(): Bitmap {
    buildDrawingCache()
    val bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    draw(Canvas(bitmap))
    return bitmap
}

fun View.enableRippleEffect() {
    context.theme.resolveAttribute(
        android.R.attr.selectableItemBackground,
        TypedValue(),
        true)
}

@MainThread
fun View.visible(duration: Long = 0L) {
    startAnimation(AlphaAnimation(0.0f, 1.0f).also {
        it.duration = duration
    })
    visibility = View.VISIBLE
}

@MainThread
fun View.gone(duration: Long = 0L) {
    startAnimation(AlphaAnimation(1.0f, 0.0f).also {
        it.duration = duration
        it.setAnimationListener(object: Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) { }
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.GONE
            }
        })
    })
}