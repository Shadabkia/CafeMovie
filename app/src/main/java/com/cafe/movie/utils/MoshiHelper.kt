package com.cafe.movie.utils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class MoshiHelper {
    private var mMoshiInstance: Moshi = getMoshi()

    private fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    companion object {
        private var mInstance: MoshiHelper? = null

        fun getInstance(): MoshiHelper {
            if (mInstance == null) {
                mInstance = MoshiHelper()
            }
            return mInstance!!
        }
    }

    fun <T> moshiFromJson(json: String, type: Type): T {
        val jsonAdapter: JsonAdapter<T> = mMoshiInstance.adapter(type)
        return jsonAdapter.fromJson(json)!!
    }

    fun <T> moshiToJson(obj: T, type: Type): String {
        val jsonAdapter: JsonAdapter<T> = mMoshiInstance.adapter(type)
        return jsonAdapter.toJson(obj)
    }
}