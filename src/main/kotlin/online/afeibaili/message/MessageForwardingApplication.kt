package online.afeibaili.message

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessageForwardingApplication

fun main(args: Array<String>) {
    runApplication<MessageForwardingApplication>(*args)
}