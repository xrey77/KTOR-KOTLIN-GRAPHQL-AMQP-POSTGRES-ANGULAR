// src/main/kotlin/com/api/graphql/KtorGraphQLContext.kt
package com.api.graphql

import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
import com.expediagroup.graphql.generator.extensions.toGraphQLContext
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import io.ktor.server.application.ApplicationCall
import graphql.GraphQLContext


// class KtorGraphQLContextFactory : DefaultKtorGraphQLContextFactory() {
//     override suspend fun generateContextMap(call: ApplicationCall): Map<*, *> {
//         return super.generateContextMap(call) + mapOf(
//             "request" to call.request
//         )
//     }
// }


class KtorGraphQLContextFactory : GraphQLContextFactory<ApplicationCall> {
    
    override suspend fun generateContext(request: ApplicationCall): GraphQLContext {
        return mapOf(
            "ktorCall" to request 
        ).toGraphQLContext()
    }
}


// import com.expediagroup.graphql.server.ktor.DefaultKtorGraphQLContextFactory
// import io.ktor.server.request.*
// import graphql.GraphQLContext

// class KtorGraphQLContextFactory : DefaultKtorGraphQLContextFactory() {
//     override suspend fun generateContext(request: ApplicationRequest): GraphQLContext {
//         val defaultContext = super.generateContext(request)
        
//         val customMap = mutableMapOf<Any, Any>()
        
//         // Example adding data: 
//         // customMap["token"] = request.headers["Authorization"] ?: ""

//         // 3. Put all values into a new GraphQLContext builder setup
//         val contextBuilder = GraphQLContext.newContext()
        
//         // Put default federated properties into the new builder
//         defaultContext.stream().forEach { (key, value) -> 
//             contextBuilder.put(key, value) 
//         }
        
//         // Put your custom properties into the builder
//         customMap.forEach { (key, value) -> 
//             contextBuilder.put(key, value) 
//         }

//         return contextBuilder.build()
//     }
// }
