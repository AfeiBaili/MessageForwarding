package online.afeibaili.message

import jakarta.websocket.CloseReason
import jakarta.websocket.Session

object SessionManager {
    val allMap = HashMap<Session, String?>()

    fun addSession(session: Session, channelName: String? = null) {
        allMap.put(session, channelName)
    }


    fun disconnect(
        session: Session,
        message: String,
        closeCode: CloseReason.CloseCodes = CloseReason.CloseCodes.NORMAL_CLOSURE,
    ) {
        session.close(CloseReason(closeCode, message))
    }

    fun removeSession(session: Session) {
        allMap.remove(session)?.let {
            ChannelManager.map[it]!!.set.remove(session)
        }
    }
}