package com.ranggacikal.solidprincipleexample.core.common

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object ConnectivityUtil {

    fun isConnected(context: Context?): Boolean = context?.run {
        hasNetworkCapabilities(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
    } ?: false

    private fun hasNetworkCapabilities(connectivityManager: ConnectivityManager) =
        connectivityManager.activeNetwork?.run {
            connectivityManager.getNetworkCapabilities(this)
        }?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } ?: false
}