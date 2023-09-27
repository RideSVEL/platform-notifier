package com.serejka.firstsearcher.controller

import lombok.extern.slf4j.Slf4j
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Slf4j
@RestController
@RequestMapping(path = ["/health"])
class HealthController {
    @GetMapping
    fun healthController(health: String): String {
        return "I am fine"
    }
}
