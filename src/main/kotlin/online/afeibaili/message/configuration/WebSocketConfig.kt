package online.afeibaili.message.configuration

import online.afeibaili.message.websocket.MessageWebSocket
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.server.standard.ServerEndpointExporter


/**
 * WebSocket的配置项
 *
 *@author AfeiBaili
 *@version 2025/7/8 16:35
 */

@Configuration
class WebSocketConfig {

    @Bean
    fun serverEndpointExporter(): ServerEndpointExporter {
        return ServerEndpointExporter().apply {
            setAnnotatedEndpointClasses(
                MessageWebSocket::class.java,
            )
        }
    }
}