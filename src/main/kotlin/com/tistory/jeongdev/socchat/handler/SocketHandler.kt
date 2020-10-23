package com.tistory.jeongdev.socchat.handler

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOError
import java.io.IOException

@Component
class SocketHandler : TextWebSocketHandler() {
    //val sessionMap: HashMap<String, WebSocketSession> = HashMap()   //웹소켓 세션 담아둘 맵
    val rls: ArrayList<HashMap<Any, Any>> = ArrayList() //웹소켓 세션을 담아둘 리스트

    //메시지 발송
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val msg: String = message.payload
        val obj: JSONObject? = jsonToObjectParser(msg)

        val roomNumber = obj?.get("roomNumber")
        var temp = HashMap<Any, Any>()
        if (rls.size > 0) {
            for (i in 0 until rls.size) {
                val tmpRoomNumber = rls[i]["roomNumber"] //세션리스트에 저장된 방번호를 가져와서
                if (roomNumber == tmpRoomNumber) {    //같은 값의 방이 존재한다면
                    temp = rls[i]   //해당 방번호의 세션리스트에 존재하는 모든 object값을 가져온다.
                    break
                }
            }

            for (i in temp.keys) {
                if (temp[i] == roomNumber) { //방번호일경우는 건너뛴다
                    continue
                }

                val wss: WebSocketSession = temp[i] as WebSocketSession
                try {
                    wss.sendMessage(TextMessage(obj!!.toJSONString()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    //소켓 연결
    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)

        var flag: Boolean = false
        val url = session.uri.toString()
        val roomNumber = url.split("/chatting/")[1]
        var idx = rls.size //방 사이즈 체크
        if (rls.size > 0) {
            for (i in 0 until rls.size) {
                val tmpRoomNumber = rls[i]["roomNumber"]
                if (tmpRoomNumber == roomNumber) {
                    flag = true
                    idx = i
                    break
                }
            }
        }

        if (flag) { //존재하는 방이면 세션만 추가
            val map: HashMap<Any, Any> = rls[idx]
            map[session.id] = session
        } else {    //신규 방이면 방번호와 세션 추가
            val map: HashMap<Any, Any> = HashMap()
            map["roomNumber"] = roomNumber
            map[session.id] = session
            rls.add(map)
        }

        //세션등록 끝나면 발급받은 세션ID값의 메시지를 발송
        val obj = JSONObject()
        obj["type"] = "getId"
        obj["sessionId"] = session.id
        session.sendMessage(TextMessage(obj.toJSONString()))

    }

    //소켓 종료
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        //소켓 종료
        if (rls.size > 0) {
            for (i in 0 until rls.size) {
                rls[i].remove(session.id)
            }
        }
        super.afterConnectionClosed(session, status)
    }

    companion object

    fun jsonToObjectParser(jsonStr: String): JSONObject? {
        val parser: JSONParser = JSONParser()
        var obj: JSONObject? = null

        try {
            obj = parser.parse(jsonStr) as JSONObject?
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }
}