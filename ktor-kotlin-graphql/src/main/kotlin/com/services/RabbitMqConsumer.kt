// src/main/kotlin/com/services/RabbitMqConsumer.kt
package com.services

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

class RabbitMqConsumer {
    private val logger = LoggerFactory.getLogger(RabbitMqConsumer::class.java)
    private val queueName = "user_login_confirmation_queue"
    private val exchangeName = "central-topic-exchange"

    // private val exchangeName = "user_events"
    

    suspend fun startListening() = withContext(Dispatchers.IO) {
        val factory = ConnectionFactory().apply {
            host = "localhost" 
            username = "guest"
            password = "guest"
        }

        try {
            val connection = factory.newConnection()
            val channel = connection.createChannel()

            channel.exchangeDeclare(exchangeName, "topic", true)
            channel.queueDeclare(queueName, true, false, false, null)
            channel.queueBind(queueName, exchangeName, "user.#")

            logger.info("RabbitMQ Consumer successfully initialized. Monitoring queue: $queueName")

            val deliverCallback = DeliverCallback { _, delivery ->
                val message = String(delivery.body, Charsets.UTF_8)
                val deliveryTag = delivery.envelope.deliveryTag
                
                try {
                    logger.info("Consumer: Message Received! Processing data: $message")
                    channel.basicAck(deliveryTag, false)
                    logger.info("Consumer: Message deliveryTag #$deliveryTag verified and ACK'd back to queue.")
                    
                } catch (e: Exception) {
                    logger.error("Failed to process incoming payload message. Rejecting.", e)
                    channel.basicNack(deliveryTag, false, true)
                }
            }

            // Start consuming messages (autoAck = false)
            channel.basicConsume(queueName, false, deliverCallback, { _ -> })

            // CRITICAL: Keeps the coroutine suspended so the listener thread stays alive
            try {
                awaitCancellation()
            } finally {
                // Ensures resources are cleaned up cleanly when application stops
                channel.close()
                connection.close()
                logger.info("RabbitMQ consumer connections closed.")
            }

        } catch (e: Exception) {
            logger.error("Failed to initialize RabbitMQ connection.", e)
        }
    }
}