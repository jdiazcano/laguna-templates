package com.example

fun main(args: Array<String>) {
    val example = Example("Hello world")
    println(example)
}

data class Example(val name: String)