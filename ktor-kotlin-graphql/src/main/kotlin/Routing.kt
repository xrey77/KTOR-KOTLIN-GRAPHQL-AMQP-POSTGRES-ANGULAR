package com.postgres.amqp


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
import com.expediagroup.graphql.server.ktor.graphQLPostRoute

fun Application.configureRouting() {
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
        // graphQLPostRoute() 
        // graphQLPostRoute(endpoint = "/graphql")


    }
}