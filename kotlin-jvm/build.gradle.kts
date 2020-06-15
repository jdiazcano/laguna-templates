plugins {
    java
    kotlin("jvm") version "1.3.70"
    application
}

apply(from = "buildSrc/gradle/repositories.gradle.kts")
apply(from = "buildSrc/gradle/sourcesets.gradle.kts")

project.ext["mainPackage"] = "com.example"
project.ext["className"] = "{{name}}"

application {
    mainClassName = "${project.ext["mainPackage"]}.${project.ext["className"]}"
}

dependencies {
    implementation(kotlin("stdlib"))
}