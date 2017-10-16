package com.ly.immortal.controller

import com.ly.immortal.domain.model.BasUser
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@RestController
@RequestMapping("test")
class TestController {
    @GetMapping(value = "/flash")
    fun testFlash(model: RedirectAttributes):ModelAndView {
        model.addAttribute("pathVariable", "testPathVariable")
        model.addFlashAttribute("model", BasUser(1, "ly"))
        return ModelAndView("redirect:/test/thymeleaf/{pathVariable}")
    }
    @GetMapping(value = "/thymeleaf/{pathVariable}")
    fun home(@PathVariable pathVariable:String,model: ModelMap):ModelAndView{
        println(pathVariable)
        val basUser :BasUser
        if(model.containsAttribute("model")){
            basUser = model["model"] as BasUser
            println(basUser)
        }else{
            basUser = BasUser(2,"fail")
            println("failed test flash redirect")
        }
        model.addAttribute("test", basUser)
        return ModelAndView("thymeleaf")
    }
}

