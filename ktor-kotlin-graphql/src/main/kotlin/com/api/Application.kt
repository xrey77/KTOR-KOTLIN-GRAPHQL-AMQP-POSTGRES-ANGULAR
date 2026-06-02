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
import kotlinx.coroutines.launch
import com.services.RabbitMqConsumer
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.application.ApplicationStopped


val TokenManagerKey = AttributeKey<TokenManager>("TokenManagerKey")

fun main(args: Array<String>) {
    System.setProperty("java.awt.headless", "true")         
    io.ktor.server.netty.EngineMain.main(args)    
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

fun Application.module() {
    val rabbitMqConsumer = RabbitMqConsumer()

    monitor.subscribe(ApplicationStarted) {
        launch {
            rabbitMqConsumer.startListening()
        }
    }    

    // monitor.subscribe(ApplicationStopped) {
    //     rabbitMqConsumer.ApplicationStopped()
    // }
    // configureSecurity() 
    // val tokenManager = TokenManager()     
    // attributes.put(TokenManagerKey, tokenManager)    

    configureRouting()  


}


@Serializable
data class ErrorResponse(val message: String)