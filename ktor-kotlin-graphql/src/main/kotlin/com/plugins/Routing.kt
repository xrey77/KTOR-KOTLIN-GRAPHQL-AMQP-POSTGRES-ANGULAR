//src/main/kotlin/com/api/plugins/Routing.kt
package com.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import java.io.File
import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources

import com.expediagroup.graphql.server.ktor.GraphQL
import com.api.graphql.queries.UserQuery
import com.api.graphql.mutations.UserMutation
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.expediagroup.graphql.server.ktor.graphQLGetRoute

// import com.expediagroup.graphql.generator.SchemaGeneratorConfig
// import com.expediagroup.graphql.generator.TopLevelObject
// import com.expediagroup.graphql.generator.toSchema
// import graphql.schema.GraphQLSchema


import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    log.info("test............................")     

    install(GraphQL) {
        schema {
            packages = listOf("com.api.graphql", "com.api.model")
            queries = listOf(UserQuery())
            mutations = listOf(UserMutation())
        }
    }

    routing {
        get("/api/test") {
            call.respondText("testing.....")
        }


        staticFiles("/static", File("users"))
        staticResources("/", "static", index = "index.html") 
        
        get("/") {
            call.respondText(
                this::class.java.classLoader.getResource("static/index.html")!!.readText(),
                ContentType.Text.Html
            )
        }

        graphiQLRoute(endpoint = "graphiql", graphQLEndpoint = "graphql")
        graphQLPostRoute(endpoint = "graphql")
        graphQLGetRoute(endpoint = "graphql")

    }
}



