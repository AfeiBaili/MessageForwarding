package online.afeibaili.message

import online.afeibaili.message.model.entity.MessageSession
import online.afeibaili.message.websocket.util.JSON

object MessageManger {

    /**
     * 解析Json消息，消息内容需要包含，频道名、uuid、消息本身
     */

    fun parsing(message: String) = runCatching {
        val value: MessageSession = JSON.readValue(message, MessageSession::class.java)
        value
    }
}