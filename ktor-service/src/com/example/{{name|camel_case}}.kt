@file:JvmName("{{name|camel_case}}")
package com.example

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080) {
        routing {
            rootRouting(this)
            demoRouting(this)
        }
    }
    server.start(wait = true)
}

val rootRouting = { routing: Routing ->
    routing.get {
        call.respondText("Hello {{name}}!", ContentType.Text.Plain)
    }
}

val demoRouting = { routing: Routing ->
    routing.get("/demo") {
        call.respondText("HELLO {{name|upper}}!")
    }
}