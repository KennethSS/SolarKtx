package com.solar.solarktx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 *  Created by Kenneth on 2020/11/13
 */

fun <T>LifecycleOwner.observe(liveData: LiveData<out T>, onChanged: (v: T) -> Unit) {
    liveData.observe(this, Observer { onChanged(it) })
}