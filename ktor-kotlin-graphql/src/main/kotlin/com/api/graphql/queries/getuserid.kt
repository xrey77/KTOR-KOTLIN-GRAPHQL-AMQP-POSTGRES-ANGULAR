// src/main/kotlin/com/api/graphql/queries/getuserid.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import com.api.model.UserModel
import com.repositories.UserTable
import com.services.RabbitMqProducer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

import com.utils.TokenManagerKey 


class UserLookupQuery : Query {
    
    suspend fun getUserById(id: String): UserModel? {
        val intId = id.toIntOrNull() ?: return null

        val user = transaction {
            UserTable
                .selectAll().where { UserTable.id eq intId }
                .map { row ->
                    UserModel(
                        id = row[UserTable.id],
                        firstname = row[UserTable.firstname],
                        lastname = row[UserTable.lastname],                        
                        email = row[UserTable.email],
                        mobile = row[UserTable.mobile],
                        username = row[UserTable.username],
                        isActive = row[UserTable.isActive],
                        isBlocked = row[UserTable.isBlocked],
                        mailtoken = row[UserTable.mailtoken],
                        userpic = row[UserTable.userpic],
                        qrcodeurl = row[UserTable.qrcodeurl]
                    )
                }
                .singleOrNull()
        }

        if (user != null) {
            val jsonPayload = buildJsonObject {
                put("eventId", java.util.UUID.randomUUID().toString())
                put("userId", user.id)
                put("event", "GETUSER_ID_VERIFIED")            
            }.toString()

            try {
                RabbitMqProducer.publishLoginEvent(jsonPayload)
            } catch (e: Exception) {
                println("Failed to publish RabbitMQ event: ${e.message}") 
            }
        }

        return user
    }
}



// getUserById - REQUEST ===============
// query GetUserById($id: String!) {
//   getUserById(id: $id) {
//     id
//     firstname
//     lastname
//     email
//     mobile
//     username
//     userpic
//     isActive
//     isBlocked
//     qrcodeurl
//   }
// }

// getUserById - VARIABLES ===============
// {
//   "id": "1"
// }
