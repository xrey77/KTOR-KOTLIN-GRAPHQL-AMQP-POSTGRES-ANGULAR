// src/main/kotlin/com/api/graphql/queries/getusers.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import com.api.model.UserModel
import com.repositories.UserTable

class UserListQuery : Query {

    fun getAllUsers(): List<UserModel> {
        return transaction {
            UserTable
                .selectAll()
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
        }
    }
}


// GetAllUsers - REQUEST ===================
// query GetAllUsers {
//   getAllUsers {
//     id
//     firstname
//     lastname
//     email
//     mobile
//     userpic
//     userpic
//     isActive
//     isBlocked
//     qrcodeurl
//   }     
// }