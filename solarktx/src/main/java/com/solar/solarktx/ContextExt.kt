package com.solar.solarktx

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
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
            Toast.makeText(this, msg, length).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, msg, length).show()
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

fun Context.goToNotificationSetting(notificationChannelId: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startActivity( Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, notificationChannelId)
        })
    } else {
        toast("버전이 너무 낮습니다.")
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

fun Context.showAlertDialog(title: String = "",
                            msg: String = "",
                            positiveText: String = getString(android.R.string.ok),
                            negativeText: String = getString(android.R.string.cancel),
                            positive: DialogInterface.OnClickListener,
                            cancelable: Boolean = true) {
    AlertDialog.Builder(ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light)).apply {
        if (title.isNotEmpty()) setTitle(title)
        if (positiveText.isNotEmpty()) setPositiveButton(positiveText, positive)
        if (negativeText.isNotEmpty()) setNegativeButton(negativeText, null)
        if (msg.isNotEmpty()) setMessage(msg)
        setCancelable(cancelable)
        create()
        show()
    }
}

fun Context.isGranted(permission: String): Boolean =
    ActivityCompat.checkSelfPermission(this, permission) == PermissionChecker.PERMISSION_GRANTED

fun Context.isGpsEnabled(): Boolean =
    (getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)

fun Context.showDialog(title: Any? = null,
                       msg: Any,
                       cancelable: Boolean = true,
                       positive: Any = "확인",
                       negative: Any = "취소",
                       positiveClick: (DialogInterface, Int) -> Unit,
                       negativeClick: (DialogInterface, Int) -> Unit = { _, _ -> } ) {
    val builder = AlertDialog.Builder(this).apply {
        setCancelable(cancelable)
    }

    when(title) {
        is String -> builder.setTitle(title)
        is Int -> builder.setTitle(title)
    }

    when(msg) {
        is String -> builder.setMessage(msg)
        is Int -> builder.setMessage(msg)
    }

    when(positive) {
        is String -> builder.setPositiveButton(positive, positiveClick)
        is Int -> builder.setPositiveButton(positive, positiveClick)
    }

    when(negative) {
        is String -> builder.setNegativeButton(negative, negativeClick)
        is Int -> builder.setNegativeButton(negative, negativeClick)
    }

    builder.create()
    builder.show()
}

fun Context.dimenToInt(id: Int): Int {
    return resources.getDimension(id).toInt()
}