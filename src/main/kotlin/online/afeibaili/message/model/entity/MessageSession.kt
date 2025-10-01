package online.afeibaili.message.model.entity


/**
 * 消息对象
 *
 *@author AfeiBaili
 *@version 2025/7/8 18:48
 */

class MessageSession {
    lateinit var uuid: String
    lateinit var name: String
    lateinit var message: String

    override fun toString(): String {
        return "MessageSession{uuid=$uuid, name='$name', message='$message'}"
    }
}