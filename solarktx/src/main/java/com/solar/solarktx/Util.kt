package com.solar.solarktx

import android.os.Looper

fun isOnMainThread() = Looper.myLooper() == Looper.getMainLooper()