package com.ly.immortal.spring.test

import com.ly.immortal.BaseTest
import org.junit.Assert.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.junit.Test as t

class MusicTest : BaseTest() {

    @Autowired
    @Qualifier("sgtPeppers")
    lateinit var sgtPeppers: CompactDisc
    @Autowired
    lateinit var cDPlayer: CDPlayer
    @t
    fun test1() {
        sgtPeppers.play()
        assertNotNull(sgtPeppers)
    }

    @t
    fun testQualifier() {
        cDPlayer.play()
    }
}