package org.example;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;


@Path("/messages")
@Singleton
public class MessageResource {

    // Implement authentication
    
    private MessageController controller;

    public MessageResource() {
        controller = new MessageController();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessages() {
        return new ArrayList<>(controller.viewMessages().values());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertMessage(@HeaderParam("userid") int userid, MessageParam message) {
        ensureUserId(userid);
        MessageId id = controller.insertMessage(userid, message.getMessage());
        return Response.status(Response.Status.CREATED).entity(id).build();
    }

    @PUT
    @Path("{messageid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editMessage(@HeaderParam("userid") int userid, @PathParam("messageid") int messageid, MessageParam message) {
        ensureUserId(userid);
        Message msg = new Message(message.getMessage(), userid);
        controller.editMessage(userid, new MessageId(messageid), msg);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{messageid}")
    public Response deleteMessage(@HeaderParam("userid") int userid, @PathParam("messageid") int messageid) {
        ensureUserId(userid);
        controller.deleteMessage(userid, new MessageId(messageid));
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    public void ensureUserId(int userid) {
        if(userid == 0) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
    }
}
