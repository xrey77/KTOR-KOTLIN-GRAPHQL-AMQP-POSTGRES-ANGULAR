// File: src/main/kotlin/com/api/AuthTokenManager.kt
package com.api

import io.ktor.util.*

interface AuthTokenManager {
    fun generateToken(username: String): String
    fun verifyToken(token: String): Boolean

    companion object {
        val AuthTokenManagerKey = AttributeKey<AuthTokenManager>("AuthTokenManagerKey")        
    }    
}