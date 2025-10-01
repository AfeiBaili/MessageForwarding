package online.afeibaili.message.websocket

import jakarta.websocket.*
import jakarta.websocket.server.ServerEndpoint
import online.afeibaili.message.SessionManager
import online.afeibaili.message.websocket.util.logger
import online.afeibaili.message.websocket.util.parsingMessage
import online.afeibaili.message.websocket.util.ungzip
import java.nio.ByteBuffer


/**
 * 消息转发主要类
 *
 *@author AfeiBaili
 *@version 2025/7/8 16:41
 */

@ServerEndpoint("/")
class MessageWebSocket : WebSocket {
    @OnOpen
    override fun onOpen(session: Session, config: EndpointConfig) {
        SessionManager.addSession(session)
        logger.info("连接进入：$session")
    }

    @OnMessage
    fun onMessageByByte(session: Session, message: ByteBuffer) {
        logger.info("收到了${message.remaining()}字节")
        val string: String = ungzip(message)
        parsingMessage(session, string, true)
    }

    @OnMessage
    override fun onMessage(session: Session, message: String) {
        logger.info("收到了一条消息：${message}")
        parsingMessage(session, message)
    }

    @OnClose
    override fun onClose(session: Session, closeReason: CloseReason) {
        SessionManager.removeSession(session)
        logger.warn("断开连接原因：${closeReason.reasonPhrase}")
    }

    @OnError
    override fun onError(session: Session, throwable: Throwable) {
        SessionManager.removeSession(session)
        logger.error("错误断开原因：${throwable.message}")
    }
}