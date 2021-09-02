package com.glivion.plasticdiary.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject

class Utils @Inject constructor(private val context: Context) {

    @RequiresApi(Build.VERSION_CODES.M)
    fun isConnectedToInternet() =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .run {
                getNetworkCapabilities(activeNetwork)?.run {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                }?: false
            }



}