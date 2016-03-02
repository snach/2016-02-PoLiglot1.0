package rest;
import main.AccountService;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Snach
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
    public Response checkSignInUser() {
        if (accountService.getOnlineUser() != null){
            String status = "{ \"id\": \"" + accountService.getOnlineUser().getUserID() + "\" }";
            System.out.append("{ \"id\": \"" + accountService.getOnlineUser().getUserID() + "\" }\n");
            return Response.status(Response.Status.OK).entity(status).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signInUser(UserProfile user, @Context HttpHeaders headers) {
        if (accountService.getOnlineUser() == null) {
            accountService.setOnlineUser(accountService.getUserByLogin(user.getLogin()));
            if (accountService.addSession(accountService.getOnlineUser())){
                String status = "{ \"id\": \"" + accountService.getOnlineUser().getUserID() + "\" }";
                return Response.status(Response.Status.OK).entity(status).build();
            } else {
                accountService.setOnlineUser(null);
            }
        }
        System.out.append("бэд реквест \n");
        return Response.status(Response.Status.BAD_REQUEST).build();

    }
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response logOut(@Context HttpServletRequest request) {
        accountService.deleteSession(accountService.getOnlineUser().getLogin());
        accountService.setOnlineUser(null);
        return Response.status(Response.Status.OK).build();
    }

}
