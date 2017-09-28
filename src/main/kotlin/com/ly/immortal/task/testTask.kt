package com.enci.merchant.service.task

import org.slf4j.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class testTask {
    val logger = LoggerFactory.getLogger(testTask::class.java)
//    @Scheduled(cron = "0 0/1 * * * ?")
    fun testJob() {
        // 间隔2分钟,执行任务
        val current = Thread.currentThread()
        println("定时任务1:" + current.id)
        logger.info("定时任务2:" + current.id)
    }

}