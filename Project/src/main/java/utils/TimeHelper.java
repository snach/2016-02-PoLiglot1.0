package utils;

/**
 * Created by Snach on 19.04.16.
 */
public class TimeHelper {
    public static void sleep(){
        if ((long) game.GameMechanicsImpl.STEP_TIME <= 0) {
            return;
        }
        try {
            Thread.sleep((long) game.GameMechanicsImpl.STEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
