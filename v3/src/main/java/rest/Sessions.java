package rest;
import main.AccountService;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
/**
 * Created by Snach on 01.03.16.
 */
@Singleton
@Path("/session")
public class Sessions {
    private AccountService accountService;

    public Sessions(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        final Collection<UserProfile> allUsers = accountService.getAllUsers();
        return Response.status(Response.Status.OK).entity(allUsers.toArray(new UserProfile[allUsers.size()])).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signInUser(UserProfile user, @Context HttpHeaders headers) {
        UserProfile onlineUser = accountService.getUserByLogin(user.getLogin());
        System.out.append("Session add: {").append(String.valueOf(onlineUser.getUserID())).append(", ")
                .append(String.valueOf(onlineUser.getLogin())).append(", ")
                .append(String.valueOf(String.valueOf(onlineUser.getPassword()))).append(", ")
                .append(String.valueOf(String.valueOf(onlineUser.getEmail()))).append("}").append('\n');

        return Response.status(Response.Status.OK).entity(onlineUser.getUserID()).build();

        /*if(accountService.addUser(user)){

            return Response.status(Response.Status.OK).entity(user.getUserID()).build();
        } else {
            System.out.append("Error: {403}");
            return Response.status(Response.Status.FORBIDDEN).build();
        }*/
    }
}
