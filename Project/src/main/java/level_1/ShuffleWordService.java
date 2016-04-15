package level_1;

import main.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import account.AccountServiceImpl;
import org.jetbrains.annotations.Nullable;
/**
 * Created by Snach on 02.04.16.
 */
public class ShuffleWordService {

    private static final int POINT = 10;


    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = new Logger(AccountServiceImpl.class);

    private final Map<String, Integer> currentScore = new HashMap<>();
    private  Map<Long, String> words = new HashMap<>();
    private long maxWordId;



    public ShuffleWordService(@Nullable Map<Long, String> map, long maxId){
        this.words = map;
        this.maxWordId = maxId;
        for (Map.Entry<Long, String> entry : words.entrySet()) {
            logger.log("id: " + entry.getKey() + " word: " + entry.getValue());
        }


    }

    public ShuffleWord getShuffleWord(){

        long randId = 1 + (long)(Math.random() * ((maxWordId - 1) + 1));
        String word = words.get(randId);
        String shuffleWord;
        do {
            shuffleWord = shuffleString(word);
        } while (word.equals(shuffleWord));

        return new ShuffleWord(randId,shuffleWord);
    }

    public String shuffleString(String word){
        char [] wordInChar = word.toCharArray ();
        Random rnd = ThreadLocalRandom.current();
        for (int i = wordInChar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            char a = wordInChar[index];
            wordInChar[index] = wordInChar[i];
            wordInChar[i] = a;
        }
        String shuffleWord = new String(wordInChar);
        logger.log(word + " -> " + shuffleWord);
        return shuffleWord;
    }


    public ShuffleWord getWordById(long id) {
        return new ShuffleWord(id,words.get(id));
    }

    public void addPointUser(String login) {
        if (!currentScore.containsKey(login)) {
            currentScore.put(login,POINT);
        } else {
            int prevscore = currentScore.get(login);
            currentScore.put(login,prevscore + POINT);
        }
    }

    public Integer getUserScoreFromMap(String login) {
        if (!currentScore.containsKey(login))
            return 0;
        int score = currentScore.get(login);
        currentScore.remove(login);
        return score;
    }
}
