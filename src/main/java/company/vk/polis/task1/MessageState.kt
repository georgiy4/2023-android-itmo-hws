package company.vk.polis.task1

sealed class MessageState {
    class Read : MessageState()
    class Unread : MessageState()
    class Deleted(val deleterId: Int) : MessageState()
}