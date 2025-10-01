package online.afeibaili.message

import jakarta.websocket.Session
import online.afeibaili.message.websocket.pojo.ChannelTable
import java.nio.ByteBuffer

object ChannelManager {
    val map = HashMap<String, ChannelTable>()

    /**
     * 获取UUID，如果map中没有UUID将创建一个新的键值对
     */
    fun getOrCreateChannel(channel: ChannelTable): String {
        map[channel.name]?.let { return it.uuid }
        map.put(channel.name, channel)
        return channel.uuid
    }

    fun sendChannelAll(session: Session, name: String, message: String) {
        val removedSet = HashSet<Session>()
        map[name]!!.set.forEach {
            if (it == session) return@forEach
            if (it.isOpen) it.asyncRemote.sendText(message)
            else removedSet.add(it)
        }
        map[name]!!.set.removeAll(removedSet)
    }

    fun sendChannelAllByBinary(session: Session, name: String, bytes: ByteBuffer) {
        val removedSet = HashSet<Session>()
        map[name]!!.set.forEach {
            if (it == session) return@forEach
            if (it.isOpen) it.asyncRemote.sendBinary(bytes.duplicate())
            else removedSet.add(it)
        }
        map[name]!!.set.removeAll(removedSet)
    }
}