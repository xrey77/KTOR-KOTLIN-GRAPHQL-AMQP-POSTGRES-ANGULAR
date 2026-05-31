// src/main/kotlin/com/api/graphql/schema/UserSchema.kt
package com.api.graphql.schema

import com.api.graphql.queries.UserQuery
import com.api.graphql.mutations.UserMutation
import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import graphql.schema.GraphQLSchema

object UserSchema {

    private val config = SchemaGeneratorConfig(
        supportedPackages = listOf("com.api.graphql.queries", "com.api.graphql.mutations", "com.api.model")
    )


    private val queries = listOf(TopLevelObject(UserQuery()))
    private val mutations = listOf(TopLevelObject(UserMutation()))

    val instance: GraphQLSchema = toSchema(
        config = config,
        queries = queries,
        mutations = mutations
    )
}
