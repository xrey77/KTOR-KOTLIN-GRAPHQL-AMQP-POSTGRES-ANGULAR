// src/main/kotlin/Exposed.kt
package com.postgres.amqp

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.api.model.Products
import com.api.model.Sales
import com.api.model.Users
import com.api.model.Roles
import com.api.model.Categories
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Application.configureExposed() {
    val driverClassName = "org.postgresql.Driver"    
    val jdbcUrl = "jdbc:postgresql://localhost:5432/ktor_kotlin_graphql"
    
    val database = Database.connect(createHikariDataSource(jdbcUrl, driverClassName))

    transaction(database) {
        SchemaUtils.create(Products, Sales, Users, Roles, Categories)            
    }
}

private fun createHikariDataSource(url: String, driver: String): HikariDataSource {
    return HikariDataSource(
        HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            username = "rey"
            password = "rey"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
    )
}
