package online.afeibaili.message.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


/**
 * 欢迎页
 *
 *@author AfeiBaili
 *@version 2025/7/8 17:20
 */

@RestController
class WelcomePage {

    @GetMapping("/")
    fun welcome(): String {
        return "Welcome"
    }
}