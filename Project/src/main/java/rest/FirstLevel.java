package rest;

import account.AccountService;
import account.UserProfile;
import level_1.ShuffleWord;
import level_1.ShuffleWordService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

/**
 * Created by Snach on 02.04.16.
 */
@Singleton
@Path("/first_level")
public class FirstLevel {
    @Inject
    private main.Context context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandShuffleWord() {
        ShuffleWord sendWord = context.get(ShuffleWordService.class).getShuffleWord();
        String status = "{ \"id\": " + sendWord.getId() + ", \"shuffle\": \""+ sendWord.getWord() + "\" }";
        return Response.status(Response.Status.OK).entity(status).build();
    }
}
