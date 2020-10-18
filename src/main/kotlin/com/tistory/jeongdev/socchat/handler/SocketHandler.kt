package com.tistory.jeongdev.socchat.handler

import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class SocketHandler : TextWebSocketHandler() {
    val sessionMap: HashMap<String, WebSocketSession> = HashMap()   //웹소켓 세션 담아둘 맵

    //메시지 발송
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val msg: String = message.payload
        for (key in sessionMap.keys) {
            val wss: WebSocketSession? = sessionMap[key]
            try {
                wss?.sendMessage(TextMessage(msg))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //소켓 연결
    override fun afterConnectionEstablished(session: WebSocketSession) {
        super.afterConnectionEstablished(session)
        sessionMap[session.id] = session
    }

    //소켓 종료
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionMap.remove(session.id)
        super.afterConnectionClosed(session, status)
    }
}