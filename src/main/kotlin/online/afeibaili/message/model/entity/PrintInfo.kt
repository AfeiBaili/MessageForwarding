package online.afeibaili.message.model.entity


/**
 * 打印信息对象
 *
 * @param channelSize 频道总数
 * @param channelAll 所有频道名
 * @param sessionAll 所有会话数
 * @param channelInfo 所有频道对应人数
 *
 *@author AfeiBaili
 *@version 2025/10/1 08:54
 */

class PrintInfo(
    val channelSize: Int,
    val channelAll: Array<String>,
    val sessionAll: Int,
    val channelInfo: Array<String>,
)