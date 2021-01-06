package com.solar.solarktx

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 *  Created by Kenneth on 2020/11/27
 */
fun RecyclerView.pagerSnap() {
    PagerSnapHelper().attachToRecyclerView(this)
}