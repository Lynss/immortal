package com.ly.immortal

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ImmortalApplication

fun main(args: Array<String>) {
    SpringApplication.run(ImmortalApplication::class.java, *args)
}