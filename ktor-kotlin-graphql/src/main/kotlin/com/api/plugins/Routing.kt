package com.api.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import com.api.graphql.schema.UserSchema
import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.types.GraphQLServerRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import io.ktor.http.ContentType
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
// import com.expediagroup.graphql.server.ktor.graphQLPostRoute
// import com.expediagroup.graphql.server.ktor.graphiQLRoute

fun Application.configureRouting() {
    routing {



        // graphQLPostRoute(endpoint = "/graphql")
        // graphiQLRoute(endpoint = "/graphiql", graphQLEndpoint = "/graphql")

    }
}