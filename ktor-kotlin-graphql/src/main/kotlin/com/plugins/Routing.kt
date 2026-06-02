//src/main/kotlin/com/api/plugins/Routing.kt
package com.plugins

import io.ktor.server.auth.authenticate
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

import com.api.graphql.queries.UserLookupQuery
import com.api.graphql.queries.UserListQuery
import com.api.graphql.queries.ProductListQuery
import com.api.graphql.queries.RootQuery
import com.api.graphql.queries.GetProductSearch
import com.api.graphql.queries.SalesDataQuery
import com.api.graphql.queries.ProductMasterDetailsQuery

import com.api.graphql.mutations.CreateUserMutation
import com.api.graphql.mutations.LoginUserMutation
import com.api.graphql.mutations.UpdateProfileMutation
import com.api.graphql.mutations.ChangePasswordMutation
import com.api.graphql.mutations.ActivateMfaMutation
import com.api.graphql.mutations.OtpMfaMutation
import com.api.graphql.mutations.UploadpicMutation

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.toSchema
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import com.expediagroup.graphql.server.ktor.graphQLGetRoute
import com.expediagroup.graphql.server.types.GraphQLRequest

import io.ktor.server.application.*
import io.ktor.server.routing.*
// import com.api.graphql.DefaultKtorGraphQLContextFactory

// import com.expediagroup.graphql.server.ktor.KtorGraphQLContextFactory

fun Application.configureRouting() {
    log.info("test............................")     
    // val contextFactory = CustomGraphQLContextFactory() 

    install(GraphQL) {
        schema {
            packages = listOf("com.api.graphql", "com.api.model")
            queries = listOf(
                UserLookupQuery(),
                UserListQuery(),
                ProductListQuery(),
                GetProductSearch(),
                SalesDataQuery(),
                ProductMasterDetailsQuery()
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
        // server {
        //     contextFactory = CustomGraphQLContextFactory()
        // }        
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

        // authenticate("auth-jwt") { 
            graphiQLRoute(endpoint = "graphiql", graphQLEndpoint = "graphql")
            graphQLPostRoute(endpoint = "graphql")
            graphQLGetRoute(endpoint = "graphql")
        // }

    }
}



