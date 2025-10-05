package online.afeibaili.message.controller

import online.afeibaili.message.ChannelManager
import online.afeibaili.message.model.entity.ChannelTable
import online.afeibaili.message.model.entity.PrintInfo
import online.afeibaili.message.websocket.util.JSON
import online.afeibaili.message.websocket.util.printInfo
import org.springframework.web.bind.annotation.*
import java.util.*


/**
 * 用于创建频道
 *
 *@author AfeiBaili
 *@version 2025/7/8 18:22
 */
@CrossOrigin
@RestController
@RequestMapping("/channel")
class ChannelController {

    /**
     * 通过名字来创建或获取一个UUID，用于发信息时传入UUID以验证
     */
    @GetMapping("/get")
    fun createChannel(@RequestParam name: String): String {
        val uuid: String = UUID.randomUUID().toString()
        return ChannelManager.getOrCreateChannel(ChannelTable(uuid, name, HashSet(), mutableListOf()))
    }

    @GetMapping("/info")
    fun getAllInfo(): String {
        val info: PrintInfo = printInfo()
        return JSON.writeValueAsString(info)
    }
}