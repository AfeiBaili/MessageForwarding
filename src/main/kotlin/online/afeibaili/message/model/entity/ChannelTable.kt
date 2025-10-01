package online.afeibaili.message.model.entity

import jakarta.websocket.Session

data class ChannelTable(val uuid: String, val name: String, val set: MutableSet<Session>)