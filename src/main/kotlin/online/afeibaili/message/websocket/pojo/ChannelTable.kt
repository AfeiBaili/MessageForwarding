package online.afeibaili.message.websocket.pojo

import jakarta.websocket.Session

data class ChannelTable(val uuid: String, val name: String, val set: MutableSet<Session>)