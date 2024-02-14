package com.cafe.movie.utils

import android.content.res.Resources

val Float.toDp get() = this / Resources.getSystem().displayMetrics.density
