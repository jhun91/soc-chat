package com.tistory.jeongdev.socchat.utils

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

class JsonToObjectParser : JSONObject() {
    companion object {
        val parser: JSONParser = JSONParser()
        val obj: JSONObject? = null


    }
}