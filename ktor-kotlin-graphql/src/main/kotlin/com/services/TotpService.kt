// // src/main/kotlin/com/services/TotpService.kt
package com.services

import dev.samstevens.totp.secret.DefaultSecretGenerator
import dev.samstevens.totp.time.SystemTimeProvider
import dev.samstevens.totp.code.DefaultCodeGenerator
import dev.samstevens.totp.code.DefaultCodeVerifier
import dev.samstevens.totp.code.HashingAlgorithm

import qrcode.QRCode
import java.util.Base64

class TotpService {
    private val secretGenerator = DefaultSecretGenerator()

    private val timeProvider = SystemTimeProvider()
    private val codeGenerator = DefaultCodeGenerator(HashingAlgorithm.SHA1)
    private val codeVerifier = DefaultCodeVerifier(codeGenerator, timeProvider).apply {
        setAllowedTimePeriodDiscrepancy(1) 
    }

    fun generateSecret(): String {
        return secretGenerator.generate()
    }

    fun getQrCodeUrl(
        secret: String,
        accountName: String,
        issuer: String
    ): String {

        val qrCodeText =
            "otpauth://totp/$issuer:$accountName" +
            "?secret=$secret" +
            "&issuer=$issuer" +
            "&algorithm=SHA1" +
            "&digits=6" +
            "&period=30"

        val pngBytes = QRCode.ofSquares()
            .withCanvasSize(200)            
            .build(qrCodeText)
            .render()
            .getBytes()

        val base64String =
            Base64.getEncoder().encodeToString(pngBytes)

        return "data:image/png;base64,$base64String"
    }

    fun verifyOtp(secret: String, code: String): Boolean {
        val cleanCode = code.replace("\\s".toRegex(), "")
        return codeVerifier.isValidCode(secret, cleanCode)
    }    
}



// package com.services

// import dev.samstevens.totp.secret.DefaultSecretGenerator
// import dev.samstevens.totp.time.SystemTimeProvider
// import dev.samstevens.totp.code.DefaultCodeGenerator
// import dev.samstevens.totp.code.DefaultCodeVerifier
// import dev.samstevens.totp.code.HashingAlgorithm

// import com.google.zxing.BarcodeFormat
// import com.google.zxing.client.j2se.MatrixToImageWriter
// import com.google.zxing.qrcode.QRCodeWriter
// import java.awt.image.BufferedImage
// import java.io.ByteArrayOutputStream
// import java.util.Base64
// import javax.imageio.ImageIO

// class TotpService {
//     private val secretGenerator = DefaultSecretGenerator()
//     private val timeProvider = SystemTimeProvider()
//     private val codeGenerator = DefaultCodeGenerator(HashingAlgorithm.SHA1)
//     private val codeVerifier = DefaultCodeVerifier(codeGenerator, timeProvider).apply {
//         setAllowedTimePeriodDiscrepancy(1) 
//     }

//     fun generateSecret(): String {
//         return secretGenerator.generate()
//     }

//     fun getQrCodeUrl(
//         secret: String,
//         accountName: String,
//         issuer: String
//     ): String {
//         val qrCodeText = "otpauth://totp/$issuer:$accountName" +
//                 "?secret=$secret" +
//                 "&issuer=$issuer" +
//                 "&algorithm=SHA1" +
//                 "&digits=6" +
//                 "&period=30"

//         // 1. Generate BitMatrix using ZXing
//         val qrCodeWriter = QRCodeWriter()
//         val bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, 200, 200)

//         // 2. Convert BitMatrix to java.awt.image.BufferedImage
//         val bufferedImage: BufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix)

//         // 3. Write BufferedImage to a ByteArrayOutputStream
//         val outputStream = ByteArrayOutputStream()
//         ImageIO.write(bufferedImage, "png", outputStream)
//         val pngBytes = outputStream.toByteArray()

//         // 4. Encode to Base64 string
//         val base64String = Base64.getEncoder().encodeToString(pngBytes)

//         return "data:image/png;base64,$base64String"
//     }

//     fun verifyOtp(secret: String, code: String): Boolean {
//         val cleanCode = code.replace("\\s".toRegex(), "")
//         return codeVerifier.isValidCode(secret, cleanCode)
//     }    
// }
