package com.ly.immortal.exception.advice

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.Exception

@ControllerAdvice
class ApplicationExceptionHandler{
    @ExceptionHandler(Exception::class)
    fun testExceptionHandler() {
        println("handle success")
    }
}
