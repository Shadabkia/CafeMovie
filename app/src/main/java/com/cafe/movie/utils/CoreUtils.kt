package com.cafe.movie.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object CoreUtils {
     fun isInternetAvailable(context: Context): Boolean {

        val applicationContext = context.applicationContext

        var result = false

        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return@let it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else {
                it.activeNetworkInfo.also { networkInfo ->
                    result = networkInfo != null && networkInfo.isConnected
                }
            }
        }

        return result
    }
}