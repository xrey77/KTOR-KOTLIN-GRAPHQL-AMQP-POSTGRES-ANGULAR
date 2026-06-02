// src/main/kotlin/com/api/graphql/mutations/verifyOtp.kt
package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
// import com.api.model.RegisterModel
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class OtpInput(
    val id: String,
    val otp: String
)

data class OtpPayload(
    val message: String,
    val username: String?
)

class OtpMfaMutation : Mutation {
    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository) 

    @GraphQLDescription("Verify OTP code in the system")
    suspend fun otpVerification(input: OtpInput): OtpPayload {
        val intId = input.id.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID format")        
        val response = userService.verifyTotp(intId, input.otp) 
        return OtpPayload(
            message = response.message,
            username = response.username
        )        
    }
}

// REQUEST
// mutation OtpVerification($input: OtpInput!) {
//   otpVerification(input: $input) {
//     message
//     username
//   }
// }

// VARIABLES
// {
//   "input": {
//     "id": "1",
//     "otp": "1334566"
//   }
// }
