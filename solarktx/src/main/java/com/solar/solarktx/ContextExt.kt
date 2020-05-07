package com.solar.solarktx

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.material.snackbar.Snackbar

fun Context.getJsonFromAsset(name: String): String {
    val inputStream = resources.assets.open(name)
    return inputStream.bufferedReader().use { it.readText() }
}

fun Context.toast(msg: Any, length: Int = Toast.LENGTH_SHORT) {
    if(msg is String) toast(msg)
    else if (msg is Int) toast(getString(msg, length))
}

fun Context.isLongResolution(): Boolean {
    val width = resources.displayMetrics.widthPixels.toFloat()
    val height = resources.displayMetrics.heightPixels.toFloat()

    //(displayMetrics.heightPixels / 16) * 10 > displayMetrics.widthPixels
    return (height / width) > 1.8979
}

fun Context.toast(id: Int, length: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), length)
}

fun Context.toast(msg: String, length: Int = Toast.LENGTH_SHORT) {
    try {
        if (isOnMainThread()) {
            Toast.makeText(applicationContext, msg, length).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, msg, length).show()
            }
        }
    } catch (e: Exception) { }
}

fun Context.isInstallApp(pkgName: String): Boolean {
    return try {
        packageManager.getPackageInfo(pkgName, PackageManager.GET_ACTIVITIES)
        true
    } catch(e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.goToPlayStore() {
    val appPackageName = packageName
    val marketUri = "market://details?id="
    val playStoreUri = "https://play.google.com/store/apps/details?id="

    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(marketUri + appPackageName)))
    } catch (anfe: android.content.ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUri + appPackageName)))
    }
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(androidx.core.content.ContextCompat.getColor(context, color)) }
}

fun Context.colorRes(res: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(res, null)
    } else {
        resources.getColor(res)
    }
}

fun Context.inflater(): LayoutInflater =
    (getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isNetworkConnect(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo

    val isConnected = activeNetwork != null && activeNetwork.isConnected

    return isConnected
}

fun Context.drawable(resId: Int): Drawable? =
    ContextCompat.getDrawable(this, resId)


fun Context.getHtmlVariable(res: Int, vararg: Any): Spanned {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(getString(res, vararg), Html.FROM_HTML_MODE_COMPACT)
    } else {
        return Html.fromHtml(getString(res, vararg))
    }
}

fun Context.hasLocationPermission(): Boolean =
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
    } else true

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.isWiFiConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } else {
        return connectivityManager.activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
    }
}


fun Context.dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun Context.showAlertDialog(title: String,
                            msg: String,
                            positiveText: String = "확인",
                            negativeText: String = "닫기",
                            positive: DialogInterface.OnClickListener) {
    AlertDialog.Builder(ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light)).apply {
        setTitle(title)
        setPositiveButton("확인", positive)
        setNegativeButton("취소", null)
        setMessage(msg)
        setCancelable(true)
        create().show()
    }
}