// src/main/kotlin/com/api/graphql/queries/getusers.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import com.api.model.UserModel
import com.repositories.UserTable
// import com.services.RabbitMqProducer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

import com.repositories.UserRepositoryImpl
import com.services.UserService

import io.ktor.server.application.*
import io.ktor.http.HttpStatusCode
import com.utils.TokenManagerKey 
import com.api.graphql.KtorGraphQLContextFactory
// import com.api.graphiql.DefaultKtorGraphQLContextFactory
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.authorization 
import io.ktor.util.Attributes
import io.ktor.server.request.header
import io.ktor.http.HttpHeaders
import graphql.schema.DataFetchingEnvironment

class UserListQuery : Query {

    private val userRepository = UserRepositoryImpl()
    private val userService = UserService(userRepository)

    // val attributes = Attributes()
    // val tokenManager = attributes.getOrNull(TokenManagerKey) 
    //     ?: throw IllegalStateException("Security module was not initialized before routing!")


    // fun getUsers(dfe: DataFetchingEnvironment): List<UserModel> {
        
        // 2. Access the GraphQL context map from the environment
        // val contextMap = dfe.graphQLContext
        
        // // 3. Extract the Ktor ApplicationCall from the context map 
        // // (The Ktor plugin automatically saves it under the ApplicationCall::class key)
        // val call = contextMap.get<ApplicationCall>(ApplicationCall::class) 
        //     ?: throw IllegalStateException("Ktor ApplicationCall not found in GraphQL Context")

        // // 4. Now you can safely use Ktor's request, application, and headers
        // val request = call.request
        // val application = call.application
        // val authorizationHeader = request.headers["Authorization"]

        // Your database/transaction and business logic goes here...
    //     return UserService.getAllUsers() 
    // }

    suspend fun getUsers(): List<UserModel> {
    // suspend fun getUsers(dfe: DataFetchingEnvironment): List<UserModel> {

        // val call = dfe.graphQLContext.get<ApplicationCall>("ktorCall") 
        //     ?: throw IllegalStateException("Ktor ApplicationCall not found in GraphQL Context")

        // // val request: ApplicationRequest = call.request
        // // val application = call.application

        // val request = call.request 
        // val application = call.application 


        // val call = contextMap.get<ApplicationCall>("ktorCall") 
        //     ?: throw IllegalStateException("Ktor ApplicationCall missing from GraphQL context")
            
        // val request: ApplicationRequest = call.request
        // val application = call.application

        // val token = request.header(HttpHeaders.Authorization)
        // if (token.isNullOrEmpty()) {
        //     throw SecurityException("Unauthorized: Invalid JWT Token")
        // }
        val userModels: List<UserModel> = userService.getAllUsers() 

        return userModels.map { userModel ->
            UserModel(
                id = userModel.id,
                firstname = userModel.firstname,
                lastname = userModel.lastname,
                email = userModel.email,
                mobile = userModel.mobile,
                username = userModel.username,
                isActive = userModel.isActive,
                isBlocked = userModel.isBlocked,
                mailtoken = userModel.mailtoken,
                userpic = userModel.userpic,
                qrcodeurl = userModel.qrcodeurl
            )        
    }
    }
}


// GetAllUsers - REQUEST ===================
// query GetUsers {
//   getUsers {
//     id
//     firstname
//     lastname
//     email
//     mobile
//     username
//     userpic
//     isActive
//     isBlocked
//     mailtoken
//     qrcodeurl    
//   }
// }