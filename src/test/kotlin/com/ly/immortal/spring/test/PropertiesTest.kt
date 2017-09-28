package com.ly.immortal.spring.test

import com.ly.immortal.BaseTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Value

class PropertiesTest:BaseTest() {
    @Value("\${config.test}")
    lateinit var test:String
    @Value("\${config.aaa.test2}")
    lateinit var test2:String
    @Test
    fun test1() {
        println(test)
        println(test2)
    }

}