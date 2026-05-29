// src/main/kotlin/com/api/graphql/mutations/UserMutations.kt
package com.api.graphql.mutations

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation

data class CreateUserInput(val firstname: String, val lastname: String, val email: String, val mobile: String, val username: String, val password: String)
data class LoginUserInput(val username: String, val password: String)

data class CreatePayload(val id: Int, val firstname: String, val lastname: String, val email: String, val mobile: String, val username: String, val password: String)
data class LoginPayload(val id: Int, val username: String, val password: String)

class UserMutations : Mutation {

    @GraphQLDescription("Creates a new user in the system")
    suspend fun createUser(input: CreateUserInput): CreatePayload {
        return CreatePayload(
            id = 1, 
            firstname = input.firstname, 
            lastname = input.lastname, 
            email = input.email, 
            mobile = input.mobile, 
            username = input.username, 
            password = input.password
        )
    }    

    @GraphQLDescription("Login a user in the system")
    suspend fun loginUser(input: LoginUserInput): LoginPayload {
        return LoginPayload(id = 1, username = input.username, password = input.password)
    }    
}
