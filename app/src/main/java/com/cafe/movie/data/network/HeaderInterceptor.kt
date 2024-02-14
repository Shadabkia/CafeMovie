package com.cafe.movie.data.network
import com.cafe.movie.data.local.*
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class HeaderInterceptor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        requestBuilder.addHeader(
            CONTENT_TYPE_HEADER_KEY, APPLICATION_JSON_HEADER_KEY
        )

        ACCESS_TOKEN.let {
            requestBuilder.addHeader(
                AUTHORIZATION_HEADER_KEY, TOKEN_PREFIX + it
            )
            Timber.tag("okhttp Header: ").i("Token: Bearer $it")
        }

        val request = requestBuilder.build()

        return chain.proceed(request)
    }

    companion object {
        const val ACCESS_TOKEN =
            "Basic YXBpa2V5OjY5Y1dxVW8wNGhpNFdMdUdBT2IzMmRXZXQjsllsVzBtSkNiwU9yLUxEamNDUXFMSzJnR29mS3plZg=="
    }
}