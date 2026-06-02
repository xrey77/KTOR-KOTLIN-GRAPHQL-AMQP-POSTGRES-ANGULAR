// src/main/kotlin/com/services/RabbitMqProducer.kt
package com.services

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel
import com.rabbitmq.client.BuiltinExchangeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.StandardCharsets
import java.io.IOException

object RabbitMqProducer {
    private const val EXCHANGE_NAME = "central-topic-exchange"
    private const val ROUTING_KEY = "user.login.success"
    private const val QUEUE_NAME = "central-topic-queue"
    private const val CONFIRM_TIMEOUT_MS = 5000L // constant (5 seconds)

    private val connection: Connection
    private val channel: Channel

    init {
        val factory = ConnectionFactory().apply {
            host = "localhost" 
            port = 5672
            username = "guest"
            password = "guest"
        }
        connection = factory.newConnection()
        channel = connection.createChannel()
        channel.confirmSelect()  // Enable publisher confirms for the  channel

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC, true)
        channel.queueDeclare(QUEUE_NAME, true, false, false, null)
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "central-topic-exchange")
        // channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "user.#")

        
    }

    /**
     * Publishes a message and waits for the RabbitMQ broker to acknowledge it.
     * @return true if acknowledged by the broker, false if nacked or timed out.
     */
    suspend fun publishLoginEvent(messageJson: String): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val body = messageJson.toByteArray(StandardCharsets.UTF_8)
            
            // Publish the message
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, body)
            
            // Wait for broker confirmation using the timeout constant
            channel.waitForConfirms(CONFIRM_TIMEOUT_MS)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }
}