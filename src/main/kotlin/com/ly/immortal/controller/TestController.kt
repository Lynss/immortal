package com.ly.immortal.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class TestController {
    @GetMapping(value = "/index")
    fun home() = "page/index.html"
}