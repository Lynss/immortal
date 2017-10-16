package com.ly.immortal.config

import com.ly.immortal.domain.test.BraveKnight
import com.ly.immortal.domain.test.Quest
import com.ly.immortal.domain.test.TestQuest
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KnightConfig {
    //大部分时候可以通过注解component进行自动的配置，不过对于有些时候，可以使用这种方式来加载bean
    @Bean
    fun knight(quest: Quest) = BraveKnight(quest)

    @Bean
    fun quest(@Value("\${config.test}") message:String) = TestQuest(message)
}