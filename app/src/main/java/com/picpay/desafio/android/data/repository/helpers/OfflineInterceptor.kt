package com.picpay.desafio.android.data.repository.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class OfflineInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (!isNetworkAvailable()) {
            val cacheControl: CacheControl = CacheControl.Builder()
                .maxStale(1, TimeUnit.DAYS)
                .build()

            request = request.newBuilder()
                .removeHeader("Cache-Control")
                .cacheControl(cacheControl)
                .build()
        }
        return chain.proceed(request)
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val cap = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
            return cap.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networks = cm.allNetworks
            for (n in networks) {
                val nInfo = cm.getNetworkInfo(n)
                if (nInfo != null && nInfo.isConnected) return true
            }
        }
        return false
    }
}

