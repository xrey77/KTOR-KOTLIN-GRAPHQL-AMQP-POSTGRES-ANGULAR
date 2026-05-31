// src/main/kotlin/Application.kt
package com.api

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.application.*
import kotlinx.serialization.Serializable
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.util.AttributeKey
import com.utils.TokenManager
import com.plugins.*

val TokenManagerKey = AttributeKey<TokenManager>("TokenManagerKey")

fun Application.module() {

    val tokenManager = TokenManager()     
    attributes.put(TokenManagerKey, tokenManager)    
    configureRouting()    
}

fun Application.configureSecurity() {
    val manager = TokenManager()
    attributes.put(TokenManagerKey, manager)
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "Access to 'protected' routes"
            verifier(manager.verifier) 
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}

@Serializable
data class ErrorResponse(val message: String)