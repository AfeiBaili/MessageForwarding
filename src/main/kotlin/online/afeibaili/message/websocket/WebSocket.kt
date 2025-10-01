package online.afeibaili.message.websocket

import jakarta.websocket.*


/**
 * WebSocket接口
 *
 *@author AfeiBaili
 *@version 2025/7/9 16:25
 */
interface WebSocket {
    fun onOpen(session: Session, config: EndpointConfig)

    fun onMessage(session: Session, message: String)

    fun onClose(session: Session, closeReason: CloseReason)

    fun onError(session: Session, throwable: Throwable)
}