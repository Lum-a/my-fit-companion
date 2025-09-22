package com.example.myfitcompanion.utils

import android.content.Context
import android.net.Uri
import com.example.myfitcompanion.appwrite.AppWriteConfig
import io.appwrite.ID
import io.appwrite.models.InputFile
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.toString

/**
 * Created by Edon Idrizi on 22/Sep/2025 :)
 **/
@Singleton
class AppWriteInteractor @Inject constructor(
    private val appWriteConfig: AppWriteConfig,
    private val context: Context
) {


    suspend fun uploadProfileImage(imageUri: Uri): Result<String> = try {
        // Create unique filename
        val filename = "profile_${UUID.randomUUID()}.jpg"

        // Convert URI to byte array
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val byteArray = inputStream?.readBytes() ?: throw Exception("Failed to read image data")
        inputStream.close()

        // Create InputFile from byte array
        val inputFile = InputFile.fromBytes(
            bytes = byteArray,
            filename = filename,
            mimeType = "image/jpeg"
        )

        // Upload to AppWrite Storage
        val uploadResult = appWriteConfig.storage.createFile(
            bucketId = AppWriteConfig.BUCKET_ID,
            fileId = ID.unique(),
            file = inputFile
        )

        // Construct the file URL manually instead of using getFileView()
        val fileUrl = "${appWriteConfig.getEndpoint()}/storage/buckets/${AppWriteConfig.BUCKET_ID}/files/${uploadResult.id}/view?project=${appWriteConfig.getProjectId()}"

        Result.success(fileUrl)
    } catch (e: Exception) {
        Result.failure(e)
    }
}