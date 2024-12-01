package org.ezpztv.androidtv.util

import android.content.Context

/**
 * Current (pixel) value as display pixels
 */
fun Int.dp(context: Context): Int = Utils.convertDpToPixel(context, this)
