// src/main/kotlin/Rabbitmq.kt
package com.postgres.amqp

import io.github.damir.denis.tudor.ktor.server.rabbitmq.RabbitMQ
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.*
import io.github.damir.denis.tudor.ktor.server.rabbitmq.rabbitMQ
import io.ktor.server.config.propertyOrNull
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import com.rabbitmq.client.ConnectionFactory
import io.ktor.server.application.*
import com.rabbitmq.client.AMQP

fun Application.configureRabbitmq() {
    val rabbitHost = environment.config.propertyOrNull("ktor.rabbitmq.host")?.getString() ?: "localhost"
    val rabbitPort = environment.config.propertyOrNull("ktor.rabbitmq.port")?.getString()?.toInt() ?: 5672
    val rabbitUser = environment.config.propertyOrNull("ktor.rabbitmq.username")?.getString() ?: "guest"
    val rabbitPassword = environment.config.propertyOrNull("ktor.rabbitmq.password")?.getString() ?: "guest"
    val rabbitVHost = environment.config.propertyOrNull("ktor.rabbitmq.virtualHost")?.getString() ?: "/"

    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> 
        val cause = throwable.cause ?: throwable
        log.error("RabbitMQ Channel Error Root Cause: ${cause.message}", cause) 
    }

    val rabbitMQScope = CoroutineScope(SupervisorJob() + exceptionHandler)
    
    install(RabbitMQ) {
        ConnectionFactory().apply {
            host = rabbitHost
            port = rabbitPort
            username = rabbitUser
            password = rabbitPassword
            virtualHost = rabbitVHost
        }              
        defaultConnectionName = "Ktor-CLI-Client" 
        dispatcherThreadPollSize = 4
        tlsEnabled = false
        scope = rabbitMQScope
    }

    rabbitmq {
        queueBind {
            queue = "dlq"
            exchange = "dlx"
            routingKey = "dlq-dlx"
            exchangeDeclare {
                exchange = "dlx"
                type = "direct"
                durable = false
            }
            queueDeclare {
                queue = "dlq"
                durable = true
            }
        }

        queueBind {
            queue = "test-queue"
            exchange = "test-exchange"
            routingKey = "test-routing-key"
            exchangeDeclare {
                exchange = "test-exchange"
                type = "direct"
                durable = false
            }
            // queueDeclare {
            //     queue = "test-queue"
            //     durable = true
            // }
        }

        queueBind {
            queue = "central-topic-queue"
            exchange = "central-topic-exchange" 
            routingKey = "central.updates.*"
            exchangeDeclare {
                exchange = "central-topic-exchange"
                type = "topic"              
                durable = true
            }
            queueDeclare {
                queue = "central-topic-queue"
                durable = true
            }
        }
        log.info("RabbitMQ is running and initialized successfully")        
    }
}