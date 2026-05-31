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
import com.api.graphql.queries.UserLookupQuery
import com.api.graphql.queries.UserListQuery

import com.api.graphql.mutations.CreateUserMutation
import com.api.graphql.mutations.LoginUserMutation
import com.api.graphql.mutations.UpdateProfileMutation
import com.api.graphql.mutations.ChangePasswordMutation
import com.api.graphql.mutations.ActivateMfaMutation
import com.api.graphql.mutations.OtpMfaMutation
import com.api.graphql.mutations.UploadpicMutation

import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*



fun Application.configureRouting() {
    log.info("test............................")     

    install(GraphQL) {
        schema {
            packages = listOf("com.api.graphql", "com.api.model")
            queries = listOf(
                UserLookupQuery(),
                UserListQuery()
            )
            mutations = listOf(
                CreateUserMutation(),
                LoginUserMutation(),
                UpdateProfileMutation(),
                ChangePasswordMutation(),
                ActivateMfaMutation(),
                OtpMfaMutation(),
                UploadpicMutation()
            )            
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



