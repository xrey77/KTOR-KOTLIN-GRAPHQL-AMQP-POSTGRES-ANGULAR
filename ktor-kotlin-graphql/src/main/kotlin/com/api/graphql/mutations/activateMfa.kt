// src/main/kotlin/com/api/graphql/mutations/activateMfa.kt
package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.api.model.RegisterModel
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class ActivateMfaInput(
    val id: String,
    val twofactorenabled: Boolean
)

data class MfaPayload(
    val message: String,
    val qrcodeurl: String?
)

class ActivateMfaMutation : Mutation {
    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository) 

    @GraphQLDescription("Update user profile in the system")
    suspend fun activateMfauth(input: ActivateMfaInput): MfaPayload {
        val intId = input.id.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID format")        
        val response = userService.activateMfa(intId, input.twofactorenabled) 
        return MfaPayload(
            message = response.message,
            qrcodeurl = response.qrcodeurl
        )        
    }
}

// REQUEST
// mutation ActivateMfauth($input: ActivateMfaInput!) {
//   activateMfauth(input: $input) {
//     message
//     qrcodeurl
//   }
// }


// VARIABLES
// {
//   "input": {
//     "id": "1",
//     "twofactorenabled": true
//   }
// }
