// src/main/kotlin/com/api/graphql/mutations/changePassword.kt
package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.api.model.RegisterModel
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class ChangePasswordInput(
    val id: String,
    val password: String, 
)

data class ChangePasswordPayload(
    val message: String
)

class ChangePasswordMutation : Mutation {
    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository) 

    @GraphQLDescription("Change user password in the system")
    suspend fun changeUserPassword(input: ChangePasswordInput): ChangePasswordPayload {
        val intId = input.id.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID format")        
        val response = userService.updatePassword(intId, input.password) 
        return ChangePasswordPayload(response ?: "Update completed with no response description.")        
    }
}


// REQUEST
// mutation ChangeUserPassword($input: ChangePasswordInput!) {
//   changeUserPassword(input: $input) {
//     message
//   }
// }


// VARIABLES
// {
//   "input": {
//     "id": "1",
//     "password": "nald"
//   }
// }
