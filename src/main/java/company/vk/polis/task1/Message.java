package company.vk.polis.task1;

record Message(Integer id, String text, Integer senderId, Long timestamp, MessageState state) implements Entity {
        public Message(Integer id, String text, Integer senderId, Long timestamp) {
                this(id, text, senderId, timestamp, new MessageState.Read());
        }

        @Override
public Integer getId() {
        return id;
        }
}