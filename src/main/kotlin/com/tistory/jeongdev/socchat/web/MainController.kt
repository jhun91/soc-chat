package com.tistory.jeongdev.socchat.web

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class MainController {

    //웹 소켓 프로그래밍 실습
    //참고 URL : https://myhappyman.tistory.com/100

    @RequestMapping("/chat")
    fun chat(): ModelAndView {
        return ModelAndView(
                "chat"
        )
    }
}