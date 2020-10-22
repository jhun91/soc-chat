package com.tistory.jeongdev.socchat.web

import com.tistory.jeongdev.socchat.model.Room
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
class MainController {

    val roomList: ArrayList<Room> = ArrayList()
    var roomNumber: Int = 0

    //웹 소켓 프로그래밍 실습
    //참고 URL : https://myhappyman.tistory.com/100

    @RequestMapping("/chat")
    fun chat(): ModelAndView {
        return ModelAndView(
                "chat"
        )
    }


    /**
     * 방 페이지
     */
    fun room(): ModelAndView {
        return ModelAndView(
            "room"
        )
    }

    /**
     * 방 생성하기
     */
    fun createRoom(@RequestParam params: HashMap<Any, Any>): List<Room> {
        val roomName = params["roomName"]
        if(roomName != null && roomName != "") {
            val room = Room()
            room.roomNumber = ++roomNumber
            room.roomName = roomName as String?
            roomList.add(room)
        }

        return roomList
    }
}