package com.ly.immortal.task

import org.slf4j.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TestTask {
    @Value("\${config.test}")
    lateinit var test:String
    @Value("\${config.aaa.test2}")
    lateinit var test2:String

    val logger = LoggerFactory.getLogger(TestTask::class.java)
    @Scheduled(cron = "0 0/1 * * * ?")
    fun testJob() {
        // 间隔2分钟,执行任务
        val current = Thread.currentThread()
        println("定时任务1:" + current.id)
        println("$test,$test2")
        logger.info("定时任务2:" + current.id)
    }
}

