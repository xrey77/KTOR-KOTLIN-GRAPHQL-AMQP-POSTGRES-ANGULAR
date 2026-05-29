package com.api

import java.io.File
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import com.api.graphql.queries.UserQuery
import com.postgres.amqp.configureRouting
import com.expediagroup.graphql.server.ktor.GraphQL
import io.ktor.server.application.*

fun Application.module() {
    install(GraphQL) {
        schema {
            packages = listOf("com.postgres.amqp")            
            queries = listOf(UserQuery()) 
            // mutations = listOf(RegisterMutationResolver())
        }
    }

    // 2. Configure routes outside the block
    configureRouting() 
}
