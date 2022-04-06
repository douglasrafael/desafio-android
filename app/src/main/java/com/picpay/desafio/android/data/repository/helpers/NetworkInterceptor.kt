package com.picpay.desafio.android.data.repository.helpers

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.proceed(chain.request())

        val cacheControl: CacheControl = CacheControl.Builder()
            .maxAge(10, TimeUnit.SECONDS)
            .build()

        request = request.newBuilder()
            .removeHeader("Cache-Control")
            .header("Cache-Control", cacheControl.toString())
            .build()
        return request
    }
}

