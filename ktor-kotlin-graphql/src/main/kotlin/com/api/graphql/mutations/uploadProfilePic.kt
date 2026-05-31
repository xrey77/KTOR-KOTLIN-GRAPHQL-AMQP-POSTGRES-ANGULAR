// src/main/kotlin/com/api/graphql/mutations/uploadProfilePic.kt
package com.api.graphql.mutations

import java.io.File
import java.util.Base64
import com.expediagroup.graphql.server.operations.Mutation
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class UploadInput(
    val id: String,
    val userpic: String?
)

data class UploadPayload(
    val message: String,
    val userpic: String?
)

class UploadpicMutation : Mutation {
    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository) 

    @GraphQLDescription("Upload profile picture in the system")
    suspend fun uploadProfilepic(userId: Int, base64Image: String?): UploadPayload {
        if (base64Image == null) throw IllegalArgumentException("No image provided")

        val cleanBase64 = base64Image.substringAfter("base64,")
        val imageBytes = Base64.getDecoder().decode(cleanBase64)

        val filename = "00$userId.png"
        val targetFile = File("users/$filename")
        targetFile.writeBytes(imageBytes)


        val response = userService.uploadProfilepic(userId, filename) 
        return UploadPayload(
            message = response.message,
            userpic = response.userpic
        )        
    }
}

