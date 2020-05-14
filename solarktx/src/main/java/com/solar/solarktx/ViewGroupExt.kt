package com.solar.solarktx

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(resId: Int, attach: Boolean = false): View =
    LayoutInflater.from(context).inflate(resId, this, attach)