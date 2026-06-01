// src/main/kotlin/com/api/graphql/queries/rootQuery.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query

class RootQuery : Query {
    fun hello(): String = "Health ok."
}