package org.example;

import java.util.Objects;

public class MessageId {

    private int messageId;

    public MessageId(int id) {
        messageId = id;
    }

    public MessageId() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageId messageId1 = (MessageId) o;
        return messageId == messageId1.messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }
}
