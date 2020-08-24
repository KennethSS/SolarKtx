package com.solar.solarktx

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import java.util.jar.Manifest

fun Activity.requestLocationPermission(requestCode: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION), requestCode)
    }
}


fun Activity.intentToCall(telNum: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNum")))
}