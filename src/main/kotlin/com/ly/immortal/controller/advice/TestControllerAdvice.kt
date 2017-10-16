package com.ly.immortal.controller.advice

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class TestControllerAdvice {
    @ExceptionHandler(Exception::class)
    fun testExceptionHandler() {
        println("catch the Exception")
    }
}