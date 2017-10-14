package com.ly.immortal

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@MapperScan(basePackages = arrayOf("com.ly,immortal.dao.mapper"), sqlSessionFactoryRef = "sqlSessionFactory")
class ImmortalApplication
fun main(args: Array<String>) {
    SpringApplication.run(ImmortalApplication::class.java, *args)
}
