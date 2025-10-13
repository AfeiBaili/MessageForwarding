package online.afeibaili.message.websocket.util

import online.afeibaili.message.Configs
import online.afeibaili.message.model.entity.ChannelTable


/**
 * 命令解析器
 *
 *@author AfeiBaili
 *@version 2025/10/13 16:34
 */

class CommandParser {
    val map = mutableMapOf<String, (ChannelTable) -> Unit>(
        "init" to { },
        "clear" to { channel ->
            channel.history.clear()
        }
    )

    fun process(message: String, channel: ChannelTable) {
        val command: String = message.removePrefix(Configs.commandPrefix)
        map[command]?.invoke(channel)
    }
}