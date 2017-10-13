package com.ly.immortal.controller

import com.ly.immortal.domain.TestThymeleaf
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class TestController {
    @GetMapping(value = "/index")
    fun home():ModelAndView{
        val modelAndView = ModelAndView("test")
        modelAndView.addObject("test", TestThymeleaf("1","2"))
        return modelAndView
    }
}