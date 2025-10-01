package online.afeibaili.message

import online.afeibaili.message.websocket.pojo.MessageSession
import online.afeibaili.message.websocket.util.json

object MessageManger {

    /**
     * 解析Json消息，消息内容需要包含，频道名、uuid、消息本身
     */

    fun parsing(message: String) = runCatching {
        val value: MessageSession = json.readValue(message, MessageSession::class.java)
        value
    }
}