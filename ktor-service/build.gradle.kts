plugins {
    java
    kotlin("jvm") version "1.3.70"
    application
}

apply(from = "buildSrc/gradle/repositories.gradle.kts")
apply(from = "buildSrc/gradle/sourcesets.gradle.kts")

project.ext["mainPackage"] = "com.example"
project.ext["className"] = "{{name|classname}}"

application {
    mainClassName = "${project.ext["mainPackage"]}.${project.ext["className"]}"
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("io.ktor:ktor-server-core:1.3.2")
    implementation("io.ktor:ktor-server-netty:1.3.2")

    testImplementation("io.mockk:mockk:1.10.0")
}