package com.postgres.amqp

import io.github.damir.denis.tudor.ktor.server.rabbitmq.RabbitMQ
import io.github.damir.denis.tudor.ktor.server.rabbitmq.dsl.*
import io.github.damir.denis.tudor.ktor.server.rabbitmq.rabbitMQ
import io.ktor.server.application.*
import io.ktor.server.config.property
import io.ktor.server.config.propertyOrNull
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

fun Application.configureRabbitmq() {
    val connectionUri: String = environment.config.propertyOrNull("rabbitmq.uri")?.getString() ?: run {
        log.info("RabbitMQ disabled, no connection URI provided")
        return
    }
    val connectionName: String = environment.config.property("rabbitmq.name").getString()
    
    val exceptionHandler = CoroutineExceptionHandler { _, throwable -> 
        log.error("ExceptionHandler got $throwable") 
    }
    val rabbitMQScope = CoroutineScope(SupervisorJob() + exceptionHandler)

    // Plugin initialization
    install(RabbitMQ) {
        uri = connectionUri
        defaultConnectionName = connectionName
        dispatcherThreadPollSize = 4
        tlsEnabled = false
        scope = rabbitMQScope
    }

    // Fixed: Consolidated infrastructure topology setup into a single block
    rabbitmq {
        // Dead Letter Queue Setup
        queueBind {
            queue = "dlq"
            exchange = "dlx"
            routingKey = "dlq-dlx"
            exchangeDeclare {
                exchange = "dlx"
                type = "direct"
            }
            queueDeclare {
                queue = "dlq"
                durable = true
            }
        }

        // Test Queue Setup (Fixed trailing cut-off error)
        queueBind {
            queue = "test-queue"
            exchange = "test-exchange"
            routingKey = "test-routing-key"
            exchangeDeclare {
                exchange = "test-exchange"
                type = "direct"
            }
            queueDeclare {
                queue = "test-queue"
                // If assigning dead letter routing to this queue:
                arguments = mapOf(
                    "x-dead-letter-exchange" to "dlx",
                    "x-dead-letter-routing-key" to "dlq-dlx"
                )
            }
        }
    }
}


