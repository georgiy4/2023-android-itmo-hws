package company.vk.polis.task1

import java.util.*

class MessageController {
    private val validEntities = Repository.getInfo().stream().filter{ e -> isValid(e) }.toArray()
    private val users: MutableMap<Int, User> = HashMap()

    init {
        for (entity in validEntities) {
            if (entity is User) {
                users.put(entity.id, entity)
            }
        }
    }

    private fun isValid(entity: Entity): Boolean =
        when(entity) {
            is Message -> entity.id != null &&  entity.text != null && entity.senderId != null
                    && entity.timestamp != null && entity.state != null
            is Chat -> entity.id != null && entity.userIds != null && entity.messageIds != null
            is User -> entity.id != null && entity.name != null
            else -> false
        }

    internal inner class ChatItem(val avatarUrl: String?, val lastMessage: Message?, val state: MessageState?) {
        fun getMessage():String? =
            if (state is MessageState.Deleted) {
                "Сообщение было удалено ${if (lastMessage?.id == null) null else users[lastMessage.id]?.name}."
            } else {
                lastMessage?.text
            }
    }

    internal fun getChatItems(userId: Int, state: MessageState? = null): List<ChatItem> {
        val result : MutableList<ChatItem> = ArrayList()
        for (entity in validEntities) {
            if (entity is Message && entity.senderId == userId && (state == null || entity.state == state)) {
                result.add(ChatItem(users[userId]?.name, entity, entity.state))
            }
        }
        return result
    }

    fun getCountOfSendMesById(userId: Int) = getChatItems(userId).size
}