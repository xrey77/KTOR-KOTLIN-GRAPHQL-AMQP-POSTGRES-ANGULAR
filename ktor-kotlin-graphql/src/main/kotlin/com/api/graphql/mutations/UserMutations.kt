// src/main/kotlin/com/api/graphql/mutations/UserMutations.kt
package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.api.model.RegisterModel
import com.api.model.dto.UserLoginDto
import com.api.model.dto.UserRegistrationDto
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.utils.TokenManager
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class CreateUserInput(val firstname: String, val lastname: String, val email: String, val mobile: String, val username: String, val password: String)
data class LoginUserInput(val username: String, val password: String)

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

data class LoginPayload(
    val id: Int, 
    val firstname: String,
    val lastname: String,
    val email: String,
    val mobile: String,
    val username: String,
    val userpic: String,
    val isActive: Boolean,
    val isBlocked: Boolean,
    val qrcodeurl: String?,
    val token: String?,
    val message: String
    )

class UserMutation : Mutation {

    val userRepository = UserRepositoryImpl()
    val userService = UserService(userRepository) 

    @GraphQLDescription("Creates a new user in the system")
    suspend fun createUser(input: CreateUserInput): CreatePayload {

        val registerModel = RegisterModel(
            id = 0,
            firstname = input.firstname,
            lastname = input.lastname,
            email = input.email,
            mobile = input.mobile,
            username = input.username,
            password = input.password,
            role_id = 0
        )

        val registrationDto = UserRegistrationDto(
            firstname = registerModel.firstname,
            lastname = registerModel.lastname,
            email = registerModel.email,
            mobile = registerModel.mobile,
            username = registerModel.username,
            password = registerModel.password
        )

        val savedUser = userService.createUser(registrationDto)

        return CreatePayload(
            id = savedUser.id,
            firstname = input.firstname, 
            lastname = input.lastname, 
            email = input.email, 
            mobile = input.mobile, 
            username = input.username, 
            password = input.password,
            message = "You have registered successfully, please login now."
        )


    }    

    @GraphQLDescription("Login a user in the system")
    suspend fun loginUser(input: LoginUserInput): LoginPayload {

        val userlogin = UserLoginDto(
            username = input.username,
            password = input.password
        )
        val user = userService.userAccount(userlogin)

        return LoginPayload(
            id = user.id, 
            firstname = user.firstname,
            lastname = user.lastname,
            email = user.email,
            mobile = user.mobile,            
            username = user.username, 
            userpic = user.userpic,
            isActive = user.isActive,
            isBlocked = user.isBlocked,
            qrcodeurl = user.qrcodeurl,
            token = user.token,
            message = "You have logged-in successfully, please wait."
        )
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



// loginUser = REQUEST ====================
// mutation LoginUser($input: LoginUserInput!) {
//   loginUser(input: $input) {
//     id
//     firstname
//     lastname
//     email
//     mobile
//     username
//     userpic
//     isActive
//     isBlocked
//     qrcodeurl    
//     token
//   }
// }


// loginUser = VARIABLES
// {
//   "input": {
//     "username": "Rey",
//     "password": "rey"
//   }
// }