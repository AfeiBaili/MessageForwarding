package online.afeibaili.message

import jakarta.websocket.Session
import online.afeibaili.message.model.entity.ChannelTable
import java.nio.ByteBuffer

/**
 * 用来管理频道
 */

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

    /**
     * 向此会话所在频道发送消息
     */
    fun sendChannelAll(session: Session, name: String, message: String) {
        val removedSet = HashSet<Session>()
        for (it in map[name]!!.set) {
            if (it == session) continue
            if (it.isOpen) it.asyncRemote.sendText(message)
            else removedSet.add(it)
        }
        map[name]!!.set.removeAll(removedSet)
    }

    /**
     * 向此会话所在频道发送二进制消息
     */
    fun sendChannelAllByBinary(session: Session, name: String, bytes: ByteBuffer) {
        val removedSet = HashSet<Session>()
        for (it in map[name]!!.set) {
            if (it == session) continue
            if (it.isOpen) it.asyncRemote.sendBinary(bytes.duplicate())
            else removedSet.add(it)
        }
        map[name]!!.set.removeAll(removedSet)
    }
}