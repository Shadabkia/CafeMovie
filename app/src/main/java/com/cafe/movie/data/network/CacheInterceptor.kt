package com.cafe.movie.data.network
import android.app.Application
import android.util.Log
import com.cafe.movie.utils.CoreUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheInterceptor @Inject constructor(private val application: Application) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (CoreUtils.isInternetAvailable(application))
            request.newBuilder().header("Cache-Control", "public, max-age=" + 1).build()
        else {
            request.newBuilder().header(
                "Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 30
            ).build()
        }
        return chain.proceed(request)
    }


}