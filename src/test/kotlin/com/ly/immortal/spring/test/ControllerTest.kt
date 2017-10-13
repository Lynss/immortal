package com.ly.immortal.spring.test

import com.ly.immortal.BaseTest
import com.ly.immortal.controller.TestController
import org.junit.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.MockMvcBuilders.*

class ControllerTest : BaseTest() {
    @Test
    fun testHome1() {
        val testController = TestController()
        val mockMvc = standaloneSetup(testController).build()
        mockMvc.perform(get("/testSpring")).andExpect(view().name("page/index.html"))
    }
}