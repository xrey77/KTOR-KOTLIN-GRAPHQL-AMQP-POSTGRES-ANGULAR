//src/main/kotlin/com/services/UserService.kt
package com.services

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.utils.TokenManager
import org.mindrot.jbcrypt.BCrypt
import com.utils.PasswordHasher
import com.api.model.dto.UserRegistrationDto
import com.api.model.dto.UserLoginDto
import com.api.model.RegisterModel
import com.api.model.LoginModel
import com.api.model.UserModel
import com.api.model.OtpVerificationResponse
import com.api.model.UploadPicModel
import com.api.model.ActivateMfaModel
import com.repositories.UserRepositoryImpl
import com.services.TotpService
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*

import io.ktor.http.*
import java.io.File
import java.util.Base64

import io.ktor.http.HttpStatusCode
import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.annotations.GraphQLDescription

import io.ktor.utils.io.* 
import kotlinx.io.*

import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation
import graphql.GraphqlErrorException


// class GraphqlCustomException(
//     override val message: String,
//     val statusCode: Int
// ) : RuntimeException(message), GraphQLError {

//     override fun getExtensions(): Map<String, Any> {
//         return mapOf(
//             "code" to statusCode,
//             "timestamp" to System.currentTimeMillis()
//         )
//     }

//     override fun getErrorType(): ErrorType = ErrorType.DataFetchingException
//     override fun getLocations(): List<SourceLocation>? = null
// }

// class GraphqlCustomException(
//     @get:GraphQLIgnore
//     override val message: String,
//     val statusCode: Int
// ) : RuntimeException(message), GraphQLError {

//     override fun getMessage(): String = message

//     override fun getExtensions(): Map<String, Any> {
//         return mapOf(
//             "code" to statusCode,
//             "timestamp" to System.currentTimeMillis()
//         )
//     }

//     override fun getErrorType(): ErrorType = ErrorType.DataFetchingException
//     override fun getLocations(): List<SourceLocation>? = null
// }


class UserService(private val userRepository: UserRepositoryImpl) {

    private val totService =  TotpService()
    private val client = HttpClient(CIO)

    suspend fun createUser(request: UserRegistrationDto): RegisterModel {

        require(request.email.contains("@")) { "Invalid email format" }
        require(request.password.length >= 3) { "Password must be at least 3 characters" }

        val existingEmail = userRepository.findByEmail(request.email)
        if (existingEmail != null) {
            throw IllegalArgumentException("Email Address has already been already taken.")
        }

        val existingUser = userRepository.findByUsername(request.username)
        if (existingUser != null) {
            throw IllegalArgumentException("Username has already been already taken.")
        }

        val hashedPassword = PasswordHasher.hash(request.password)
        val rolename = userRepository.findRoleByName("ROLE_USER")
        
        val newUser = RegisterModel(
            id = 0,
            firstname = request.firstname,
            lastname = request.lastname,
            email = request.email,
            mobile = request.mobile ?: "",
            username = request.username,
            password = hashedPassword,
            role_id = rolename.id
        )

        return userRepository.save(newUser)         
    }

    suspend fun userAccount(request: UserLoginDto): LoginModel {
        require(request.password.length >= 3) { "Password must be at least 3 characters" }

        val existingUser = userRepository.findLoginUsername(request.username)
        if (existingUser != null) {

            val isPasswordValid = PasswordHasher.checkPassword(request.password, existingUser.password)
            if (!isPasswordValid) {
                throw IllegalArgumentException("Invalid password, please try again.")
            }

            val tokenManager = TokenManager()
            val tokenid = tokenManager.generateToken(existingUser.username)

            val loginModel = LoginModel(
                id = existingUser.id,
                firstname = existingUser.firstname,
                lastname = existingUser.lastname,
                email = existingUser.email,
                mobile = existingUser.mobile,
                username = existingUser.username,
                password = "",
                isActive = existingUser.isActive,
                isBlocked = existingUser.isBlocked,
                mailtoken = existingUser.mailtoken,
                userpic = existingUser.userpic,
                secret = existingUser.secret,
                qrcodeurl = existingUser.qrcodeurl,
                token = tokenid
            )

            return loginModel

        } else {
            throw IllegalArgumentException("User not found, please register now.")
        }

    }


    // suspend fun userAccount(request: UserLoginDto): ServiceResponse<LoginModel> {
    //     return  try {

    //         require(request.password.length >= 3) { "Password must be at least 3 characters" }

    //         val sanitizedUsername = request.username.trim()
    //         val existingUser = userRepository.findLoginUsername(sanitizedUsername) //?: throw Exception("Invalid credentials")
    //         if (existingUser == null) {
    //             return ServiceResponse(
    //                 status = HttpStatusCode.Unauthorized,
    //                 message = "Username not found, please register now."
    //             )
    //         }


    //             val isPasswordValid = BCrypt.checkpw(request.password.trim(), existingUser.password) 
    //             // ?: throw Exception("Invalid credentials")
    //             if (!isPasswordValid) {
    //                 return ServiceResponse(
    //                     status = HttpStatusCode.Unauthorized,
    //                     message = "Invalid password, please try again."
    //                 )
    //             }

    //             val tokenManager = TokenManager()
    //             val tokenid = tokenManager.generateToken(existingUser.username)

    //             val loginModel = LoginModel(
    //                 id = existingUser.id,
    //                 firstname = existingUser.firstname,
    //                 lastname = existingUser.lastname,
    //                 email = existingUser.email,
    //                 mobile = existingUser.mobile,
    //                 username = existingUser.username,
    //                 password = "",
    //                 isActive = existingUser.isActive,
    //                 isBlocked = existingUser.isBlocked,
    //                 mailtoken = existingUser.mailtoken,
    //                 userpic = existingUser.userpic,
    //                 secret = existingUser.secret,
    //                 qrcodeurl = existingUser.qrcodeurl,
    //                 token = tokenid
    //             )

    //             ServiceResponse(
    //                 status = HttpStatusCode.OK,
    //                 data = loginModel
    //             )

    //     } catch (e: Exception) {
    //         ServiceResponse(
    //             status = HttpStatusCode.InternalServerError,
    //             message = e.localizedMessage ?: "An unexpected error occurred"
    //         )
    //     }                
    // }

    // suspend fun getUserData(id: Int): UserModel {
    //     val existingUser = userRepository.findUserById(id)
    //     if (existingUser != null) {

    //         val userModel = UserModel(
    //             id = existingUser.id,
    //             firstname = existingUser.firstname,
    //             lastname = existingUser.lastname,
    //             email = existingUser.email,
    //             mobile = existingUser.mobile,
    //             username = existingUser.username,
    //             isActive = existingUser.isActive,
    //             isBlocked = existingUser.isBlocked,
    //             mailtoken = existingUser.mailtoken,
    //             userpic = existingUser.userpic,
    //             qrcodeurl = existingUser.qrcodeurl
    //         )

    //         return userModel

    //     } else {
    //         throw IllegalArgumentException("User not found, please register now.")
    //     }
    // }    

    suspend fun updateUserProfile(id: Int, firstname: String, lastname: String, mobile: String): String? {
        val updatedRows = userRepository.findUserById(id)
        if (updatedRows == null) {
            throw IllegalArgumentException("User not found, please register now.")
        } 
        userRepository.updateProfile(id, firstname, lastname, mobile)        
        return "You have updated your profile successfully."
    }    


    suspend fun updatePassword(id: Int, pword: String): String? {
        val updatedRows = userRepository.findUserById(id)
        if (updatedRows == null) {
            throw IllegalArgumentException("User not found, please register now.")
        } 

        val hashedPwd = PasswordHasher.hash(pword)

        userRepository.changePassword(id, hashedPwd)
        return "You have changed your password successfully."        
        
    }    


    suspend fun activateMfa(id: Int, twofactorenabled: Boolean): ActivateMfaModel {
        val userData = userRepository.findUserById(id)
        if (userData == null) {
            throw IllegalArgumentException("User not found, please register now.")
        } 

        if (twofactorenabled) {
            // println("Mfa Enabled......")            
            val secret = totService.generateSecret()
            val b64qrcode = totService.getQrCodeUrl(secret, userData.email, "Arab Bank")
            userRepository.activateMfa(id, twofactorenabled, secret, b64qrcode)
            val result = ActivateMfaModel(message="Multi-Factor Authenticator has been enabled.",qrcodeurl=b64qrcode)
            return result
        } else {
            // println("Mfa Disabled......")
            val secret = ""
            val qrcodeurl = ""
            userRepository.activateMfa(id, twofactorenabled, secret, qrcodeurl)
            val result = ActivateMfaModel(message="Multi-Factor Authenticator has been disabled.",qrcodeurl=null)
            return result
        }
    }    

    suspend fun verifyTotp(id: Int, otp: String): OtpVerificationResponse {
        val checkUserid = userRepository.findOtpVerification(id)
        if (checkUserid == null) {
            throw IllegalArgumentException("User not found, please register now.")
        } 
        if (checkUserid.secret == null) {
            throw IllegalArgumentException("Multi-Factor Authenticator is not yet enabled.")
        } 

        val res = totService.verifyOtp(checkUserid.secret, otp)
        if (res) {
            return OtpVerificationResponse(message="OTP code veried successfully.", username = checkUserid.username)
        } else {
            throw IllegalArgumentException("Invalid OTP code, please try again.")
        }
    }

    suspend fun uploadProfilepic(id: Int, userpic: String): UploadPicModel {
       var updatepic = userRepository.uploadUpdateProfilepic(id, userpic)
       return UploadPicModel(
            message="You have change your profile picture successfully.",
            userpic=userpic
       )
    }



}
