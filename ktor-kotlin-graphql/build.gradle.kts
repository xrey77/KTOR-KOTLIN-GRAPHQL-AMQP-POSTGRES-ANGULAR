
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.postgres.amqp"
version = "1.0.0-SNAPSHOT"

application {
    applicationDefaultJvmArgs = listOf("-Dlogback.configurationFile=logback-custom.xml")    
    mainClass.set("io.ktor.server.tomcat.jakarta.EngineMain")    
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=ALL-UNNAMED",
        "-Dio.ktor.development=true"
    )    
}

tasks.withType<JavaExec> {
    systemProperty("java.awt.headless", "true")
}

kotlin {
    jvmToolchain(21)
}
val ktor_version = "3.5.0"

dependencies {
    val exposedVersion = "0.50.0"    
    implementation("io.ktor:ktor-server-tomcat-jakarta:3.5.0")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    implementation("io.ktor:ktor-server-config-yaml-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-config-yaml-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")    
    implementation("io.ktor:ktor-http-jvm:$ktor_version")

    implementation("com.expediagroup:graphql-kotlin-ktor-server:9.1.0")

    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion") 
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.exposed:exposed-crypt:0.50.0")         
    implementation("org.mindrot:jbcrypt:0.4")


    implementation("com.zaxxer:HikariCP:5.1.0")
    implementation("org.postgresql:postgresql:42.7.10")


    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.requestValidation)
    implementation(libs.damirdenisTudor.ktorServerRabbitmq)
    implementation(libs.h2database.h2)
    implementation(libs.h2database.r2dbc)
    implementation(libs.logback.classic)
    implementation(libs.postgresql)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
