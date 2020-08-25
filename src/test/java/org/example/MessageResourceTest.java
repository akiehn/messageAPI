package org.example;

import junit.framework.TestCase;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MessageResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(MessageResource.class);
    }

    @Test
    public void test_get_list_of_messages_returns_Status_OK_and_list_of_correct_length() {
        target("/messages").request().header("userid", 123)
                .post(Entity.json(new MessageParam("newly added message")));
        target("/messages").request().header("userid", 345)
                .post(Entity.json(new MessageParam("another newly added message")));

        Response response = target("/messages").request().get();

        List<Message> messages = readMessages(response);

        assertEquals("Http Response should be 200: ",
                Response.Status.OK.getStatusCode(), response.getStatus());

        assertEquals(2, messages.size());
    }

    private List<Message> readMessages(Response response) {
        return response.readEntity(new GenericType<List<Message>>() {});
    }

    @Test
    public void test_add_new_message() {
        Response response = target("/messages").request().header("userid", 123)
                .post(Entity.json(new MessageParam("newly added message")));

        assertThat(getMessages(),hasItem(new Message("newly added message",123)));

        assertEquals("Http Response should be 201 ", Response.Status.CREATED.getStatusCode(), response.getStatus());

    }

    @Test
    public void test_delete_message() {
        MessageParam messageToBeChanged = new MessageParam("newly added message");
        Response newMessageResponse = target("/messages").request().header("userid", 123)
                .post(Entity.json(messageToBeChanged));
        MessageId messageId = newMessageResponse.readEntity(MessageId.class);


        Response response = target("/messages/" + messageId.getMessageId()).request().header("userid", 123)
                .delete();

        assertEquals("Http Response should be 204 ", Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void test_edit_message() {
        MessageParam messageToBeChanged = new MessageParam("message to be changed");
        Response newMessageResponse = target("/messages").request().header("userid", 123)
                .post(Entity.json(messageToBeChanged));
        MessageId messageId = newMessageResponse.readEntity(MessageId.class);


        assertThat(getMessages(),hasItem(new Message("message to be changed",123)));


        MessageParam changedMessage = new MessageParam("this is a changed message");
        Response response = target("/messages/" + messageId.getMessageId()).request().header("userid", 123)
                .put(Entity.json(changedMessage));

        assertEquals("Http Response should be 204 ", Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        assertThat(getMessages(),hasItem(new Message("this is a changed message",123)));
        assertThat(getMessages(),not(hasItem(new Message("message to be changed",123))));
    }

    @Test
    public void test_unauthorised_access_when_adding_new_message() {
        Response response = target("/messages").request()
                .post(Entity.json(new MessageParam("very important message")));

        assertEquals("Http Response should be 401 ", Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void test_unauthorised_access_when_editing_message() {
        Response response = target("/messages/" + 321).request()
                .put(Entity.json(new MessageParam("very important message")));

        assertEquals("Http Response should be 401 ", Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

    }

    @Test
    public void test_unauthorised_access_when_deleting_message() {
        Response response = target("/messages/" + 0).request()
                .delete();

        assertEquals("Http Response should be 401 ", Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());

    }

    @Test
    public void test_delete_of_your_own_message() {
        //Given
        Response newMessageResponse = target("/messages").request().header("userid", 123)
                .post(Entity.json(new MessageParam("message to be deleted")));
        MessageId messageId = newMessageResponse.readEntity(MessageId.class);
        Message messageToBeDeleted = new Message("message to be deleted", 123);
        assertThat(getMessages(),hasItem(messageToBeDeleted));

        //When
        Response deleteResponse = target("/messages/" + messageId.getMessageId()).request().header("userid", 123)
                .delete();
        //Then
        assertEquals("Http Response should be 204 ", Response.Status.NO_CONTENT.getStatusCode(), deleteResponse.getStatus());
        assertThat("List should not contain id of message", getMessages(), not(hasItem(messageToBeDeleted)));
    }

    private List<Message> getMessages() {
        return readMessages(target("/messages").request().get());
    }

    @Test
    public void test_delete_other_users_message_fails() {
        Response newMessageResponse = target("/messages").request().header("userid", 123)
                .post(Entity.json(new MessageParam("undeleted message")));
        MessageId messageId = newMessageResponse.readEntity(MessageId.class);

        Response deleteResponse = target("/messages/" + messageId.getMessageId()).request().header("userid", 456)
                .delete();

        assertEquals("Http Response should be 401 ", Response.Status.UNAUTHORIZED.getStatusCode(), deleteResponse.getStatus());
        assertThat("Messages should not be deleted ", getMessages(),hasItem(new Message("undeleted message",123)));
    }

    @Test
    public void test_edit_other_users_message_fails() {
        Response newMessageResponse = target("/messages").request().header("userid", 123)
                .post(Entity.json(new MessageParam("newly added message")));
        MessageId messageId = newMessageResponse.readEntity(MessageId.class);
        List<Message> messages = getMessages();

        Response editResponse = target("/messages/" + messageId.getMessageId()).request().header("userid", 456)
                .put(Entity.json(new MessageParam("edit someone's message")));

        assertEquals("Http Response should be 401 ", Response.Status.UNAUTHORIZED.getStatusCode(), editResponse.getStatus());
        assertEquals("Messages should be unchanged ", messages,getMessages());
    }

    @Test
    public void test_edit_returns_not_found_when_message_does_not_exist() {
        Response editResponse = target("/messages/" + 42).request().header("userid", 456)
                .put(Entity.json(new MessageParam("edited message")));

        assertEquals("Http Response should be 404 ", Response.Status.NOT_FOUND.getStatusCode(), editResponse.getStatus());
    }

    @Test
    public void test_delete_returns_not_found_when_message_does_not_exist() {
        Response editResponse = target("/messages/" + 42).request().header("userid", 456)
                .delete();

        assertEquals("Http Response should be 404 ", Response.Status.NOT_FOUND.getStatusCode(), editResponse.getStatus());
    }

}