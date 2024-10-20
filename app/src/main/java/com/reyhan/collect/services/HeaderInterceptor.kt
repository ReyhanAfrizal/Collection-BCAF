package com.reyhan.collect.services

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-Api-Key", "E7B7EEFDB1BAB10E444DBAAAB3A8A1BE")
            .build()
        return chain.proceed(request)
    }
}