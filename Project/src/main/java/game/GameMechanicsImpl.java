package game;

import base.GameMechanics;
import frontend.WebSocketServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.TimeHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.*;

/**
 * Created by Snach on 19.04.16.
 */
public class GameMechanicsImpl implements GameMechanics {

    private static final int STEP_TIME = 100;

    @SuppressWarnings("PointlessArithmeticExpression")
    private static final int GAME_TIME = 1 * 60 * 1000;

    private static final Logger LOGGER = LogManager.getLogger(GameMechanicsImpl.class);

    @NotNull
    private final WebSocketServiceImpl webSocketService;

    @NotNull
    private final Map<String, GameSession> nameToGame = new HashMap<>();

    @NotNull
    private final Set<GameSession> allSessions = new HashSet<>();

    @Nullable
    private volatile String waiter;

    public GameMechanicsImpl(@NotNull WebSocketServiceImpl webSocketService) {
        this.webSocketService = webSocketService;

    }

    @Override
    public int getMyScore(String user){
        return nameToGame.get(user).getMyScore(user);
    }

    @Override
    public int getEnemyScore(String user){
        return nameToGame.get(user).getEnemyScore(user);
    }

    public String getEnemyName(String user){
        return nameToGame.get(user).getEnemy(user).getMyName();
    }

    @Override
    public void addUser(@NotNull String user) {
        if (waiter != null) {
            //noinspection ConstantConditions
            starGame(user, waiter);
            waiter = null;
        } else {
            waiter = user;
        }
    }

    public void removeGameSession(@NotNull String user){
        allSessions.remove(nameToGame.get(user));
        nameToGame.remove(user);
    }

    @Override
    public void removeUser(@NotNull String user) {
        if (waiter != null && user.equals(waiter)){
            waiter = null;
        }
        final GameSession myGameSession = nameToGame.get(user);
        if (myGameSession != null) {
            final GameUser enemyUser = myGameSession.getEnemy(user);
            webSocketService.notifyEnemyLeft(enemyUser);
        }
    }

    @Override
    public void incrementScore(String userName) {
        GameSession myGameSession = nameToGame.get(userName);
        GameUser myUser = myGameSession.getSelf(userName);
        myUser.incrementMyScore();
        GameUser enemyUser = myGameSession.getEnemy(userName);
        enemyUser.incrementEnemyScore();
    }


    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            gmStep();
            TimeHelper.sleep(STEP_TIME);
        }
    }

    @Override
    public void gmStep() {
        for (GameSession session : allSessions) {
            if (session.getSessionTime() > GAME_TIME) {
                LOGGER.info("игра завершена для : " + session.getFirst().getMyName() + " и " + session.getSecond().getMyName());
                if (session.isEquality()) {
                    webSocketService.notifyGameOver(session.getFirst(), true, false);
                    webSocketService.notifyGameOver(session.getSecond(), true, false);
                } else {
                    boolean firstWin = session.isFirstWin();
                    webSocketService.notifyGameOver(session.getFirst(), false, firstWin);
                    webSocketService.notifyGameOver(session.getSecond(), false, !firstWin);
                }
                nameToGame.values().removeAll(Collections.singleton(session));
                allSessions.remove(session);
            }
        }
    }

    @Override
    public void starGame(@NotNull String first, @NotNull String second) {
        GameSession gameSession = new GameSession(first, second);
        allSessions.add(gameSession);
        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        webSocketService.notifyStartGame(gameSession.getSelf(first));
        webSocketService.notifyStartGame(gameSession.getSelf(second));
    }
}
