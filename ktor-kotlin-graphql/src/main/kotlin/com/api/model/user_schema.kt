package com.api.model

import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.* 

data class UserRow(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val email: String,
    val mobile: String,
    val username: String,
    val password: String,
    val isActive: Boolean,
    val isBlocked: Boolean,  
    val mailtoken: Int,
    val userpic: String,
    val secret: String?,
    val qrcodeurl: String?,
    val roleId: Int,   
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

object Roles : Table("roles") {
    val id = integer(name="id").autoIncrement()
    val name = varchar("name", length = 100)
    override val primaryKey = PrimaryKey(id)
}

object Users : Table("users") {
    val id = integer(name="id").autoIncrement()
    val firstname = varchar("firstname", length = 50).nullable()
    val lastname = varchar("lastname", length = 50).nullable()
    val email = varchar("email", length = 100).uniqueIndex()
    val mobile = varchar("mobile", length = 50).nullable()
    
    val username = varchar("username", length = 50).uniqueIndex()    
    val password = varchar("password", length = 100,)
    val isActive = bool("isActive").default(true)
    val isBlocked = bool("isBlocked").default(false)
    val mailtoken = integer(name="mailtoken").default(0)
    val userpic = varchar("userpic", length = 50).default("pix.png")
    
    val secret = text("secret").nullable()
    val qrcodeurl = text("qrcodeurl").nullable()
    
    val role = integer("role_id").references(Roles.id)         

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}
