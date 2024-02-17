package com.cafe.movie.data.network

import com.cafe.movie.data.local.*
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class HeaderInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader(
            CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_HEADER_KEY
        )

        requestBuilder.addHeader(
            AUTHORIZATION_HEADER_KEY, TOKEN_PREFIX + ACCESS_TOKEN
        )
        Timber.tag("okhttp Header: ").i("Token: Bearer $ACCESS_TOKEN")

        val request = requestBuilder.build()

        return chain.proceed(request)
    }

    companion object {
        const val ACCESS_TOKEN =
            "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlZTU0Y2Q1MTYxZGQ5ZWY5MjVjNzU2ODlmYWVhNDM2YyIsInN1YiI6IjY1Y2FkOWJjODliNTYxMDE4NDY4Y2Q2OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.HFxnSzJ8FpABnzUhN9ig1f2zcvEi_39NOdeIR6cG5BQ"
    }
}