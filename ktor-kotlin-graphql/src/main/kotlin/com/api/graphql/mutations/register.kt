package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.api.model.RegisterModel
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class CreateUserInput(
    val firstname: String, 
    val lastname: String, 
    val email: String, 
    val mobile: String, 
    val username: String, 
    val password: String
)

data class CreatePayload(
    val id: Int,    
    val firstname: String, 
    val lastname: String, 
    val email: String, 
    val mobile: String, 
    val username: String, 
    val password: String,
    val message: String
)

class CreateUserMutation : Mutation {
    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository) 

    @GraphQLDescription("Creates a new user in the system")
    suspend fun createUser(input: CreateUserInput): CreatePayload {
        // Your logic here to call userService and return CreatePayload
        return CreatePayload(1, input.firstname, input.lastname, input.email, input.mobile, input.username, input.password, "User created successfully")
    }
}


// createUser - REQUEST ===========================
// mutation CreateUser($input: CreateUserInput!) { 
//   createUser(input: $input) { 
//     message
//   } 
// }


// createUser - VARIABLES
// {
//   "input": {
//   "firstname": "Rey",
//   "lastname": "Gragasin",
//   "email": "rey@yahoo.com",
//   "mobile": "23423423",
//   "username": "Rey",
//   "password": "rey"
//   }
// }
