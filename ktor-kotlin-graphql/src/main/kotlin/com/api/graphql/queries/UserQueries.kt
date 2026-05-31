//src/main/kotlin/com/api/graphql/queries/UserQueries.kt
package com.api.graphql.queries

import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import com.api.model.UserModel
import com.repositories.UserTable

class UserQuery : Query {
    
    fun getUserById(id: String): UserModel? {
        val intId = id.toIntOrNull() ?: return null
        return transaction {
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
    }

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

data class User(val id: String, val name: String)



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