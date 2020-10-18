package com.tistory.jeongdev.socchat.web

import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class MainControllerTests {
    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @LocalServerPort
    val port: Int = 0

    @Test
    fun chatTest1() {
        val url = "http://localhost:$port/chat"

        //val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(url, requestDto, String)
        val responseEntity: ResponseEntity<String> = restTemplate.getForEntity(url, String)

        assertThat(responseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(responseEntity.body).contains("채팅")
        //logger.debug { responseEntity.body }
    }
}