// src/main/kotlin/com/api/graphql/schema/UserSchema.kt
package com.api.graphql.schema

import com.api.graphql.queries.UserQuery
import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import graphql.GraphQL

object UserSchema {
    // 1. Configure the schema generator packages
    private val config = SchemaGeneratorConfig(
        supportedPackages = listOf("com.api.graphql", "com.api.model")
    )

    // 2. Register your query classes
    private val queries = listOf(
        TopLevelObject(UserQuery())
    )

    // 3. Generate the native GraphQL schema object
    val graphQLSchema: GraphQL = GraphQL.newGraphQL(
        toSchema(config = config, queries = queries)
    ).build()
}
