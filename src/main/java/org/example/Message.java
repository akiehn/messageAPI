package org.example;

import java.util.Objects;

public class Message {

    private int userId;
    private String message;

    public Message(String message, int userId) {
        this.userId = userId;
        this.message = message;
    }

    public Message() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return userId == message1.userId &&
                Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, message);
    }

    @Override
    public String toString() {
        return "Message{" +
                "userId=" + userId +
                ", message='" + message + '\'' +
                '}';
    }
}
