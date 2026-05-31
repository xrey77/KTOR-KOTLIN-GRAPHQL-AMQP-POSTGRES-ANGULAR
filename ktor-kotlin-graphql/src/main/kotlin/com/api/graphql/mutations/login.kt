package com.api.graphql.mutations

import com.expediagroup.graphql.server.operations.Mutation
import com.repositories.UserRepositoryImpl
import com.services.UserService
import com.api.model.dto.UserLoginDto
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

data class LoginUserInput(val username: String, val password: String)

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

class LoginUserMutation : Mutation {
    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository)

    @GraphQLDescription("Logs in an existing user")
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