// src/main/kotlin/com/api/graphql/mutations/updateProfile.kt
package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.api.model.RegisterModel
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class UserProfileInput(
    val id: String,
    val firstname: String, 
    val lastname: String, 
    val mobile: String
)

data class ProfilePayload(
    val message: String
)

class UpdateProfileMutation : Mutation {
    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository) 

    @GraphQLDescription("Update user profile in the system")
    suspend fun updateUserProfile(input: UserProfileInput): ProfilePayload {
        val intId = input.id.toIntOrNull() ?: throw IllegalArgumentException("Invalid ID format")        
        val response = userService.updateUserProfile(intId, input.firstname, input.lastname, input.mobile) 
        return ProfilePayload(response ?: "Update completed with no response description.")        
    }
}

// REQUEST
// mutation UpdateUserProfile($input: UserProfileInput!) {
//   updateUserProfile(input: $input ){
//     message
//   }
// }


// VARIABLES
// {
//   "input": {
//   	"id": "1",    
//     "firstname": "Reynaldo",
//     "lastname": "Marquez",
//     "mobile": "+63343434"    
//   }
// }

