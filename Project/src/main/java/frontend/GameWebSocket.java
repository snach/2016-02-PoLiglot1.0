package frontend;

import base.AccountService;
import com.google.gson.*;
import com.sun.istack.internal.Nullable;
import game.firstlvl.ShuffleWord;
import game.firstlvl.ShuffleWordService;
import game.GameMechanicsImpl;
import game.GameUser;
import main.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;

/**
 * Created by Snach on 19.04.16.
 */

@WebSocket
public class GameWebSocket {

    private static final Logger LOGGER = LogManager.getLogger(GameWebSocket.class);

    @Nullable
    private Session session;
    @NotNull
    private final GameMechanicsImpl gameMechanics;
    private final WebSocketServiceImpl webSocketService;
    private final ShuffleWordService shuffleWordService;
    private final AccountService accountService;

    @NotNull
    private final String myName;
    public GameWebSocket(@NotNull String myName, Context context) {
        this.myName = myName;
        this.gameMechanics = context.get(GameMechanicsImpl.class);
        this.webSocketService = context.get(WebSocketServiceImpl.class);
        this.shuffleWordService = context.get(ShuffleWordService.class);
        this.accountService = context.get(AccountService.class);
        LOGGER.info("Socket created for " + myName);


    }
    @NotNull
    public String getMyName() {
        return myName;
    }

    public void startGame(@NotNull GameUser user) {

            final JsonObject json = new JsonObject();
            json.addProperty("action", "start");
            json.addProperty("user", user.getMyName());
            json.addProperty("enemy", user.getEnemyName());

            sendJson(json);
    }

    public void gameOver(boolean equality, boolean win) {

        final JsonObject jsonEndGame = new JsonObject();
        jsonEndGame.addProperty("status", "finishGame");
        if (equality){
            jsonEndGame.addProperty("equality", true);
        } else {
            jsonEndGame.addProperty("win", win);
        }
        int currentScore = gameMechanics.getMyScore(myName);
        jsonEndGame.addProperty("mySore", currentScore);
        jsonEndGame.addProperty("enemySore", gameMechanics.getEnemyScore(myName));

        int prevScore = accountService.getUserByLogin(myName).getScore();
        boolean isBestScore;

        if (currentScore > prevScore){
            accountService.editScore(myName,currentScore);

            isBestScore = true;
        } else {
            isBestScore = false;
        }
        jsonEndGame.addProperty("best", isBestScore);

        sendJson(jsonEndGame);

    }

    public void finishGameEnemyleft() {
        final JsonObject json = new JsonObject();
        json.add("action", new JsonPrimitive("enemyLeft"));
        sendJson(json);
    }

    @SuppressWarnings("unused")
    @OnWebSocketMessage
    public void onMessage(String data) {
        try {
            final JsonElement jsonElement = new JsonParser().parse(data);
            final String action = jsonElement.getAsJsonObject().getAsJsonPrimitive("action").getAsString();
            if (action == null){
                throw new JsonSyntaxException("Can't find out \"action\" in JSON");
            }
            switch (action){
                case "getWord":
                    ShuffleWord word = shuffleWordService.getShuffleWord();
                    final JsonObject jsonWord = new JsonObject();
                    jsonWord.addProperty("action", "getWord");
                    jsonWord.addProperty("id", word.getId());
                    jsonWord.addProperty("word", word.getWord());
                    sendJson(jsonWord);
                    break;

                case "checkWord":

                    final long idWord = jsonElement.getAsJsonObject().getAsJsonPrimitive("id").getAsLong();
                    final String userWord = jsonElement.getAsJsonObject().getAsJsonPrimitive("word").getAsString();
                    ShuffleWord rightWord = shuffleWordService.getWordById(idWord);
                    boolean check;
                    if (rightWord.getWord().equals(userWord)) {
                        check = true;
                        gameMechanics.incrementScore(myName);
                    } else {
                        check = false;
                    }
                    final JsonObject jsonCheck = new JsonObject();
                    jsonCheck.addProperty("action", "checkWord");
                    jsonCheck.addProperty("answer", check);
                    jsonCheck.addProperty("right", rightWord.getWord());
                    jsonCheck.addProperty("mySore", gameMechanics.getMyScore(myName));
                    jsonCheck.addProperty("enemySore", gameMechanics.getEnemyScore(myName));

                    sendJson(jsonCheck);
                    break;

                default:
                    throw new JsonSyntaxException("Unknown \"action\"");
            }
        } catch(JsonSyntaxException e){

            LOGGER.error("Can't find out \"action\" in JSON");
            final JsonObject jsonError = new JsonObject();
            jsonError.addProperty("error", "action is null");
            sendJson(jsonError);
        }
    }

    @SuppressWarnings({"ParameterHidesMemberVariable", "unused"})
    @OnWebSocketConnect
    public void onOpen(@NotNull Session session) {
        this.session = session;
        webSocketService.addUser(this);
        gameMechanics.addUser(myName);

    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        webSocketService.removeUser(this);
        gameMechanics.removeUser(myName);
        LOGGER.info("Closing socket status: {} reason: {}", statusCode, reason);
    }

    public void sendJson(JsonObject json){
        try {
            if (session != null && session.isOpen())
                session.getRemote().sendString(json.toString());
        } catch (IOException | WebSocketException e){
            LOGGER.error("Can't send web socket", e);
        }
    }
}