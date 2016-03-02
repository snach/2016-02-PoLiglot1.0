package rest;

import main.AccountService;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * Created by snach
 */
@Singleton
@Path("/user")
public class Users {
    private AccountService accountService;

    public Users(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        final Collection<UserProfile> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new UserProfile[allUsers.size()])).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByID(@PathParam("id") long id) {
        final UserProfile user = accountService.getUser(id);
        if(user == null){
            return Response.status(Response.Status.FORBIDDEN).build();
        }else {
            String status = "{ \"id\": " + user.getUserID() + ",\n" + "\"login\": \""
                    + user.getLogin() + "\",\n" + "\"email\": \""+ user.getEmail() + "\" }";
            return Response.status(Response.Status.OK).entity(status).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserProfile user, @Context HttpHeaders headers){
        if(accountService.addUser(user)){
            String status = "{ \"id\": \"" + user.getUserID() + "\" }";
            return Response.status(Response.Status.OK).entity(status).build();
        } else {
            System.out.append("Error: {403}\n");
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editUser(UserProfile user,@PathParam("id") long id, @Context HttpHeaders headers) {
        if (accountService.getOnlineUser().getUserID() != id || accountService.getOnlineUser() == null){
            String status = "{ \"status\": \"403\", \"message\": \"Чужой юзер\" }";
            return Response.status(Response.Status.FORBIDDEN).entity(status).build();
        } else {
            accountService.editUser(accountService.getOnlineUser(),user);
            String status = "{ \"id\": \"" + user.getUserID() + "\" }";
            return Response.status(Response.Status.OK).entity(status).build();

        }
    }
    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") long id, @Context HttpHeaders headers) {
        if (accountService.getOnlineUser().getUserID() != id){
            String status = "{ \"status\": \"403\", \"message\": \"Чужой юзер\" }";
            return Response.status(Response.Status.FORBIDDEN).entity(status).build();
        } else {
            accountService.deleteSession(accountService.getUser(id).getLogin());
            accountService.deleteUser(id);
            return Response.status(Response.Status.OK).build();

        }
    }
}