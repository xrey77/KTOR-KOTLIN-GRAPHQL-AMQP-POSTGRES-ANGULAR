// src/main/kotlin/com/api/graphql/mutations/register.kt
package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.api.model.RegisterModel
import com.api.model.dto.UserRegistrationDto
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.services.RabbitMqProducer
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

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

        val registerModel = UserRegistrationDto(
            firstname = input.firstname,
            lastname = input.lastname,
            email = input.email,
            mobile = input.mobile,
            username = input.username,
            password = input.password
        )

        val savedUser = userService.createUser(registerModel) 

        val jsonPayload = buildJsonObject {
            put("eventId", java.util.UUID.randomUUID().toString())
            put("userId", savedUser.id)
            put("event", "USER_REGISTRATION_VERIFIED")
        }.toString()

        try {
            RabbitMqProducer.publishLoginEvent(jsonPayload)
        } catch (e: Exception) {
            println("Failed to publish login event to RabbitMQ: ${e.message}")
        }

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
