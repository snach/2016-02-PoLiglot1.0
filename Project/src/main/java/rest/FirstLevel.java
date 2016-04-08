package rest;


import account.AccountService;
import account.UserProfile;
import level_1.ShuffleWord;
import level_1.ShuffleWordService;

import level_1.WordRequest;
import main.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Created by Snach on 02.04.16.
 */
@Singleton
@Path("/first_level")
public class FirstLevel {
    @Inject
    private main.Context context;

    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = new Logger(FirstLevel.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRandShuffleWord() {
        ShuffleWord sendWord = context.get(ShuffleWordService.class).getShuffleWord();
        String status = "{ \"id\": " + sendWord.getId() + ", \"shuffle\": \""+ sendWord.getWord() + "\" }";
        return Response.status(Response.Status.OK).entity(status).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAnswerUser(WordRequest userWord, @Context HttpHeaders headers, @Context HttpServletRequest request) {

        String sessionID = request.getSession().getId();

        if (context.get(AccountService.class).isLoggedIn(sessionID)) {

            ShuffleWord rightWord = context.get(ShuffleWordService.class).getWordById(userWord.getId());
            boolean check;
            if (rightWord.getWord().equals(userWord.getWord())) {
                check = true;
                context.get(ShuffleWordService.class).addPointUser(userWord.getLogin());
            } else {
                check = false;
            }

            String status = "{ \"answer\": " + check + ", \"right\": " + rightWord.getWord() + " }";
            return Response.status(Response.Status.OK).entity(status).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUserScore(UserProfile user, @Context HttpHeaders headers, @Context HttpServletRequest request) {

        String sessionID = request.getSession().getId();

        if (context.get(AccountService.class).isLoggedIn(sessionID)) {

            int currentScore = context.get(ShuffleWordService.class).getUserScoreFromMap(user.getLogin());
            int prevScore = context.get(AccountService.class).getUserByLogin(user.getLogin()).getScore();
            boolean isBestScore;
            if (currentScore > prevScore) {
                isBestScore = true;
                context.get(AccountService.class).editScore(user.getLogin(), currentScore);
            } else {
                isBestScore = false;
            }
            String status = "{ \"score\": " + currentScore + ", \"best\": " + isBestScore + " }";
            return Response.status(Response.Status.OK).entity(status).build();

        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

}

