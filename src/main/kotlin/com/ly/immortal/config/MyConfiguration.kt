package com.ly.immortal.config

import com.ly.immortal.domain.test.BraveKnight
import com.ly.immortal.domain.test.SgtPeppers
import org.springframework.context.annotation.*
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.util.concurrent.Executor
import java.util.concurrent.Executors


@Configuration
@EnableScheduling
//甚至阔以用importResource来加载xml配置
//一般来说可以创建一个总的config来导入所有的配置，然后在启动的时候只需要通过加载这个配置就可以了
//@Import(KnightConfig::class)
//似乎不需要就能自动扫描组件,应该是Springboot中有自动扫描了启动类的包
@ComponentScan(basePackageClasses = arrayOf(BraveKnight::class, SgtPeppers::class))
//@PropertySource(value = "classpath:config/config.properties")
@PropertySources(
        PropertySource(value = "classpath:config/config.properties"),
        PropertySource(value = "classpath:config/a.properties"))
class MyConfiguration {
    /**
     * properties读取配置
     */
    @Bean
    fun propertyConfigInDev(): PropertySourcesPlaceholderConfigurer = PropertySourcesPlaceholderConfigurer()

    /**
     *spring-task多线程配置
     */
    fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor())
    }

    @Bean
    fun taskExecutor(): Executor {
        return Executors.newScheduledThreadPool(100)
    }
}