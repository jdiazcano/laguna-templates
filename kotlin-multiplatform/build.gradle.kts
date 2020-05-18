import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    kotlin("multiplatform") version "1.3.70"
    kotlin("plugin.serialization") version "1.3.70"
}

apply(from = "buildSrc/gradle/repositories.gradle.kts")

project.ext["mainPackage"] = "com.example"
project.ext["className"] = "{{name}}"

val mingwPath = File(System.getenv("MINGW64_DIR") ?: "C:/msys64/mingw64")

val currentOs = when {
    Os.isFamily(Os.FAMILY_MAC) -> "macos"
    Os.isFamily(Os.FAMILY_UNIX) -> "linux"
    Os.isFamily(Os.FAMILY_WINDOWS) -> "windows"
    else -> TODO()
}

kotlin {
    val jvm = jvm {
        withJava()
        val jvmJar by tasks.getting(org.gradle.jvm.tasks.Jar::class) {
            doFirst {
                manifest {
                    attributes["Main-Class"] = "${project.ext["mainPackage"]}.{{name}}Kt"
                }
                from(configurations.getByName("runtimeClasspath").map { if (it.isDirectory) it else zipTree(it) })
            }
        }
    }

    val linux = linuxX64("linux") {
        binaries {
            executable("{{name}}") {
                entryPoint = "${project.ext["mainPackage"]}.main"
            }
        }
    }

    val macos = macosX64("macos") {
        binaries {
            executable("{{name}}") {
                entryPoint = "${project.ext["mainPackage"]}.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("common/src")
            dependencies {
                implementation(kotlin("stdlib-common", "1.3.70"))
            }
        }

        val commonTest by getting {
            kotlin.srcDir("common/tst")
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val native by creating {
            kotlin.srcDir("common-native/src")
            dependsOn(commonMain)
            dependencies {
            }
        }

        val nativeTest by creating {
            kotlin.srcDir("common-native/tst")
            dependsOn(commonTest)
            dependencies {
            }
        }

        val linuxMain by getting {
            kotlin.srcDirs("linux/src")
            dependsOn(native)
        }

        val linuxTest by getting {
            kotlin.srcDirs("linux/tst")
            dependsOn(nativeTest)
        }

        val macosMain by getting {
            kotlin.srcDir("macos/src")
            dependsOn(native)
        }

        val macosTest by getting {
            kotlin.srcDirs("macos/tst")
            dependsOn(nativeTest)
        }

        val jvmMain by getting {
            kotlin.srcDir("jvm/src")
            dependsOn(commonMain)
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        val jvmTest by getting {
            kotlin.srcDir("jvm/tst")
            dependsOn(commonTest)
            dependencies {
            }
        }
    }
}

val release by tasks.registering {
    dependsOn("link${"{{name}}".capitalize()}DebugExecutable${currentOs.capitalize()}")
    dependsOn("link${"{{name}}".capitalize()}ReleaseExecutable${currentOs.capitalize()}")
}