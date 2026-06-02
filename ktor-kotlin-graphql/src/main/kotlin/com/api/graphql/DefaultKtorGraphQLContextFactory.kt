// src/main/kotlin/com/api/graphql/DefaultKtorGraphQLContextFactory.kt
package com.api.graphql

import com.expediagroup.graphql.generator.extensions.plus
import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import io.ktor.server.request.ApplicationRequest
import graphql.GraphQLContext

class CustomGraphQLContextFactory : DefaultKtorGraphQLContextFactory() {
    override suspend fun generateContext(request: ApplicationRequest): GraphQLContext {
        // Retrieve the default context (which automatically includes federated tracing)
        val baseContext = super.generateContext(request)
        
        // If you need to fetch Ktor's ApplicationCall, you can access it via request.call
        // val call = request.call 
                
        // To append custom data, use the .plus() extension function:
        // return baseContext.plus(mapOf("yourKey" to "yourValue"))
        
        return baseContext
    }
}
