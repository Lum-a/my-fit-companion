package com.example.myfitcompanion.appwrite

import android.content.Context
import io.appwrite.Client
import io.appwrite.services.Storage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Edon Idrizi on 22/Sep/2025 :)
 **/
@Singleton
class AppWriteConfig @Inject constructor(private val context: Context) {

    private val client by lazy {
        Client(context)
            .setEndpoint(APPWRITE_PUBLIC_ENDPOINT) // Your AppWrite endpoint
            .setProject(APPWRITE_PROJECT_ID) // Replace with your AppWrite project ID
    }

    val storage by lazy { Storage(client) }

    companion object {
        const val BUCKET_ID = "68d15f9b002d8a3055d6" // Replace with your bucket ID
        const val APPWRITE_PROJECT_ID = "68d1576e001bef2f2c8a"
        const val APPWRITE_PROJECT_NAME = "MyFitCompanion"
        const val APPWRITE_PUBLIC_ENDPOINT = "https://fra.cloud.appwrite.io/v1"
    }

    fun getEndpoint(): String = APPWRITE_PUBLIC_ENDPOINT
    fun getProjectId(): String = APPWRITE_PROJECT_ID
}
