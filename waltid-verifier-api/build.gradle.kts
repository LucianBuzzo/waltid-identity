/*import com.github.jk1.license.filter.DependencyFilter
import com.github.jk1.license.filter.LicenseBundleNormalizer
import com.github.jk1.license.render.InventoryHtmlReportRenderer
import com.github.jk1.license.render.ReportRenderer*/
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val KOTLIN_VERSION = "1.9.21" // also change 2 plugins
    const val KTOR_VERSION = "2.3.6" // also change 1 plugin
    const val COROUTINES_VERSION = "1.7.3"
    const val EXPOSED_VERSION = "0.43.0"
    const val HOPLITE_VERSION = "2.8.0.RC3"
}

plugins {
    kotlin("jvm") // Versions.KOTLIN_VERSION
    id("org.jetbrains.kotlin.plugin.serialization") // Versions.KOTLIN_VERSION

    id("io.ktor.plugin") version "2.3.6" // Versions.KTOR_VERSION
    id("org.owasp.dependencycheck") version "8.4.3"
    id("com.github.jk1.dependency-license-report") version "2.5"
    application
    `maven-publish`

    id("com.github.ben-manes.versions")
}

group = "id.walt"
version = "1.SNAPSHOT"

repositories {
    mavenCentral()
    //jcenter()
    maven("https://jitpack.io")
    maven("https://maven.walt.id/repository/waltid/")
    maven("https://maven.walt.id/repository/waltid-ssi-kit/")
    maven("https://repo.danubetech.com/repository/maven-public/")
    maven("https://maven.walt.id/repository/waltid/id/walt/core-crypto/")
    maven("https://maven.walt.id/repository/waltid/id/walt/waltid-ssikit2")
    mavenLocal()
}


dependencies {

    /* -- KTOR -- */

    // Ktor server
    implementation("io.ktor:ktor-server-core-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-auth-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-sessions-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-auto-head-response-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-double-receive-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-host-common-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-status-pages-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-compression-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-cors-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-forwarded-header-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-call-logging-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-call-id-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-server-cio-jvm:${Versions.KTOR_VERSION}")

    // Ktor server external libs
    implementation("io.github.smiley4:ktor-swagger-ui:2.7.1")

    // Ktor client
    implementation("io.ktor:ktor-client-core-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-serialization-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-content-negotiation:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-json-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-cio-jvm:${Versions.KTOR_VERSION}")
    implementation("io.ktor:ktor-client-logging-jvm:${Versions.KTOR_VERSION}")


    /* -- Kotlin -- */

    // Kotlinx.serialization
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.KTOR_VERSION}")

    // Date
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES_VERSION}")

    /* -- Misc --*/

    // Config
    implementation("com.sksamuel.hoplite:hoplite-core:${Versions.HOPLITE_VERSION}")
    implementation("com.sksamuel.hoplite:hoplite-hocon:${Versions.HOPLITE_VERSION}")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
    implementation("org.slf4j:slf4j-simple:2.0.9")
    implementation("org.slf4j:jul-to-slf4j:2.0.9")
    implementation("io.ktor:ktor-client-cio-jvm:${Versions.KTOR_VERSION}")

    // Test
    testImplementation(kotlin("test"))
    //testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.KOTLIN_VERSION}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES_VERSION}")
    //testImplementation("io.ktor:ktor-server-tests-jvm:${Versions.KTOR_VERSION}")

    // CLI
    //implementation("com.github.ajalt.clikt:clikt:4.2.0")

    // OIDC
    api(project(":waltid-openid4vc"))

    // SSIKit
    //implementation("id.walt:waltid-ssikit:1.2309171812.0")
    //implementation("id.walt:waltid-sd-jwt:1.2306191408.0")
    //implementation("id.walt.servicematrix:WaltID-ServiceMatrix:1.1.3")

    // SSI Kit 2
    api(project(":waltid-crypto"))
    api(project(":waltid-verifiable-credentials"))
    api(project(":waltid-did"))
    // implementation("id.walt:waltid-ssikit2:1.0.8-SNAPSHOT")
    // implementation("id.walt:core-crypto:1.0.7-SNAPSHOT")
}

tasks.withType<Test> {
    useJUnitPlatform()

    // Use the following condition to optionally run the integration tests:
    // > gradle build -PrunIntegrationTests
    if (!project.hasProperty("runIntegrationTests")) {
        exclude("id/walt/test/integration/**")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.named<CreateStartScripts>("startScripts") {
    doLast {
        windowsScript.writeText(
            windowsScript.readText().replace(Regex("set CLASSPATH=.*"), "set CLASSPATH=%APP_HOME%\\\\lib\\\\*")
        )
    }
}

application {
    mainClass.set("id.walt.verifier.MainKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            pom {
                name.set("walt.id verifier")
                description.set(
                    """
                    Kotlin/Java library for the walt.id verifier
                    """.trimIndent()
                )
                url.set("https://walt.id")
            }
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://maven.walt.id/repository/waltid/")
            val envUsername = System.getenv("MAVEN_USERNAME")
            val envPassword = System.getenv("MAVEN_PASSWORD")

            val usernameFile = File("secret_maven_username.txt")
            val passwordFile = File("secret_maven_password.txt")

            val secretMavenUsername = envUsername ?: usernameFile.let { if (it.isFile) it.readLines().first() else "" }
            val secretMavenPassword = envPassword ?: passwordFile.let { if (it.isFile) it.readLines().first() else "" }

            credentials {
                username = secretMavenUsername
                password = secretMavenPassword
            }
        }
    }
}

//licenseReport {
//    renderers = arrayOf<ReportRenderer>(InventoryHtmlReportRenderer("waltid-verifier-licenses-report.html", "walt.id verifier"))
//    filters = arrayOf<DependencyFilter>(LicenseBundleNormalizer())
//}
