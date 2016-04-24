package game;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Snach on 19.04.16.
 */
public class GameUser {
    private static final int POINT = 10;
    @NotNull
    private final String myName;
    @NotNull
    private String enemyName;
    private int myScore = 0;
    private int enemyScore = 0;

    public GameUser(@NotNull String myName, @NotNull String enemyName) {

        this.myName = myName;
        this.enemyName = enemyName;
    }

    @NotNull
    public String getMyName() {
        return myName;
    }
    @NotNull
    public String getEnemyName() {
        return enemyName;
    }

    public int getMyScore() {
        return myScore;
    }

    public int getEnemyScore() {
        return enemyScore;
    }

    public void incrementMyScore() {
        myScore += POINT;
    }

    public void incrementEnemyScore() {
        enemyScore += POINT;
    }

    public void setEnemyName(@NotNull String enemyName) {
        this.enemyName = enemyName;
    }
}
