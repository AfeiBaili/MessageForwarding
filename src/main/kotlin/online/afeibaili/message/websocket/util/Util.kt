package online.afeibaili.message.websocket.util

import com.fasterxml.jackson.databind.json.JsonMapper
import jakarta.websocket.Session
import online.afeibaili.message.ChannelManager
import online.afeibaili.message.Configs
import online.afeibaili.message.MessageManger
import online.afeibaili.message.SessionManager
import online.afeibaili.message.model.entity.ChannelTable
import online.afeibaili.message.model.entity.MessageSession
import online.afeibaili.message.model.entity.PrintInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * 一些工具方法和属性
 *
 *@author AfeiBaili
 *@version 2025/7/8 18:03
 */

val JSON: JsonMapper = JsonMapper()

val logger: Logger = LoggerFactory.getLogger("Channel")

enum class ParsingMessageResult() {
    SUCCESS,
    FAILED
}

val commandParser = CommandParser()

/**
 * 校验消息的准确性，如果正确将发送信息
 */
fun parsingMessage(session: Session, message: String, isBinary: Boolean = false): ParsingMessageResult {
    val messageSession: MessageSession = MessageManger.parsing(message).onFailure { exception ->
        SessionManager.disconnect(
            session,
            "格式错误，正确格式需要包含uuid、频道名、消息内容。错误信息：${exception.message}"
        )
        return ParsingMessageResult.FAILED
    }.getOrElse { exception ->
        SessionManager.disconnect(
            session,
            "不可为null：${exception.message}"
        )
        return ParsingMessageResult.FAILED
    }
    ChannelManager.map[messageSession.name]?.let { it ->
        if (messageSession.uuid != it.uuid) {
            SessionManager.disconnect(
                session,
                "频道uuid校验错误"
            )
            return ParsingMessageResult.FAILED
        }

        ChannelManager.sendHistory(session, messageSession.name)
        it.set.add(session)
        SessionManager.allMap.put(session, messageSession.name)
        if (parsingCommand(messageSession, it)) {
            return ParsingMessageResult.SUCCESS
        }
        ChannelManager.map[messageSession.name]?.history?.add(messageSession.message)

        //发送模块、如果是二进制就进行压缩
        if (isBinary) ChannelManager.sendChannelAllByBinary(session, messageSession.name, gzip(messageSession.message))
        else ChannelManager.sendChannelAll(session, messageSession.name, messageSession.message)
        logger.info(messageSession.toString())
        return ParsingMessageResult.SUCCESS
    }

    SessionManager.disconnect(session, "没有所谓的频道，请使用\"/channel/get?name=频道名\"创建")
    return ParsingMessageResult.FAILED
}

fun parsingCommand(messageSession: MessageSession, channel: ChannelTable): Boolean {
    if (messageSession.message.startsWith(Configs.commandPrefix)) {
        commandParser.process(messageSession.message, channel)
        return true
    }
    return false
}

fun printInfo(): PrintInfo {
    return PrintInfo(
        ChannelManager.map.size,
        ChannelManager.map.keys.toTypedArray(),
        SessionManager.allMap.size,
        ChannelManager.map.map { (name, set) -> "$name=$set" }.toTypedArray()
    )
}

fun ungzip(byte: ByteBuffer): String {
    val bytes = ByteArray(byte.remaining())
    byte.get(bytes)
    GZIPInputStream(ByteArrayInputStream(bytes)).use {
        return it.bufferedReader(Charsets.UTF_8).readText()
    }
}

fun gzip(text: String): ByteBuffer {
    val bytes = text.toByteArray(Charsets.UTF_8)
    val outputStream = ByteArrayOutputStream()
    GZIPOutputStream(outputStream).use {
        it.write(bytes)
    }
    return ByteBuffer.wrap(outputStream.toByteArray())
}