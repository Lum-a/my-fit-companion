package com.example.myfitcompanion.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object MoshiInteractor {
    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    inline fun <reified T> toJson(model: T): String? {
        val adapter = moshi.adapter(T::class.java)
        return adapter.toJson(model)
    }

    inline fun <reified T> fromJson(json: String): T? {
        val adapter = moshi.adapter(T::class.java)
        return adapter.fromJson(json)
    }
}