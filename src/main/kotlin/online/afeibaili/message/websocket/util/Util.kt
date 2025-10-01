package online.afeibaili.message.websocket.util

import com.fasterxml.jackson.databind.json.JsonMapper
import jakarta.websocket.Session
import online.afeibaili.message.ChannelManager
import online.afeibaili.message.MessageManger
import online.afeibaili.message.SessionManager
import online.afeibaili.message.websocket.pojo.MessageSession
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

val json: JsonMapper = JsonMapper()

val logger: Logger = LoggerFactory.getLogger("Channel")

enum class ParsingMessageResult() {
    SUCCESS,
    FAILED
}

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
        it.set.add(session)
        SessionManager.allMap.put(session, messageSession.name)
        if (isBinary) ChannelManager.sendChannelAllByBinary(session, messageSession.name, gzip(messageSession.message))
        else ChannelManager.sendChannelAll(session, messageSession.name, messageSession.message)
        logger.info(messageSession.toString())
        return ParsingMessageResult.SUCCESS
    }

    SessionManager.disconnect(session, "没有所谓的频道，请使用\"/channel/get?name=频道名\"创建")
    return ParsingMessageResult.FAILED
}

fun printInfo() {
    logger.info("当前的频道数量：${ChannelManager.map.size}")
    logger.info("全部人数：${SessionManager.allMap}")
    logger.info(
        "频道人数：${
            ChannelManager.map.values.joinToString(" | ") { it ->
                " ${it.name}：${it.set.size} "
            }
        }")
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