package com.ly.immortal.spring.test

import com.ly.immortal.BaseTest
import com.ly.immortal.config.KnightConfig
import com.ly.immortal.domain.test.BraveKnight
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.junit.Test as t

//也可以注解的方式加载配置文件
//@ContextConfiguration(classes = arrayOf(KnightConfig::class))
class KnightTest : BaseTest() {
    @Autowired
    lateinit var knight: BraveKnight
    @t
    fun springIOC() {
        val context = AnnotationConfigApplicationContext(KnightConfig::class.java)
//        val knight =context.getBean("knight") as BraveKnight
        knight.embarkQuest()
    }

}