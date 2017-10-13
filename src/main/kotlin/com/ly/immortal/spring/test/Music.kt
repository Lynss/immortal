package com.ly.immortal.spring.test

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

interface CompactDisc {
    fun play()
}

@Component("sgtPeppers")//默认值就这个
@Profile("prod")
class SgtPeppers : CompactDisc {
    private val titel = "sgt pepper..."
    private val artist="the Beatles"
    override fun play() {
        println("$artist 表演 $titel")
    }
}

@Component("lyDisco")//默认值就这个
@Profile("prod")
class LyDisco : CompactDisc {
    private val titel = "LyDisco..."
    private val artist="ly"
    override fun play() {
        println("$artist 表演 $titel")
    }
}
@Component("cDPlayer")//默认值就这个
class CDPlayer(val lyDisco:CompactDisc){
    fun play() = lyDisco.play()
}