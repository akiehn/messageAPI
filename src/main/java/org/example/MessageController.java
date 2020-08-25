package org.example;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class MessageController {

    private HashMap<MessageId, Message> messages;
    public IdGenerator idGenerator = new IdGenerator();

    public MessageController() {
        this.messages = new HashMap<>();
    }


    public MessageId insertMessage(int userId, String message) {
        MessageId msgId = new MessageId(idGenerator.createId());
        Message msg = new Message(message, userId);
        messages.put(msgId, msg);
        return msgId;
    }

    public void editMessage(int userId, MessageId msgID, Message newMsg) {
        Message msg = messages.get(msgID);
        if (msg == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);        }
        if (userId == msg.getUserId()) {
            messages.put(msgID, newMsg);
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    public void deleteMessage(int userid, MessageId messageid) {
        Message msg = messages.get(messageid);
        if (msg == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        if (userid == msg.getUserId()) {
            messages.remove(messageid);
        } else {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }

    public HashMap<MessageId, Message> viewMessages() {
        return messages;
    }

}
