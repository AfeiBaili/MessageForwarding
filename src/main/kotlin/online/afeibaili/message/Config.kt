package online.afeibaili.message

import online.afeibaili.message.websocket.util.logger

private val map = mutableMapOf<String, (String) -> Unit>(
    "--history" to { value ->
        runCatching {
            val bool: Boolean = value.toBoolean()
            Configs.history = bool
            logger.info("历史记录状态：${value}")
        }
    })

fun passingArgs(array: Array<String>) {
    array.forEach { config ->
        val split: List<String> = config.split("=")
        if (split.size != 2) return@forEach
        val configName = split[0]
        val configValue = split[1]
        runCatching {
            map[configName]!!.invoke(configValue)
        }.onFailure {
            logger.error("不存在的配置：$configName")
        }
    }
}

object Configs {
    var history: Boolean = false
}