package level_1;


import account.UserProfile;
import account.UserProfileDAO;
import main.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import account.AccountServiceImpl;
import org.jetbrains.annotations.Nullable;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Created by Snach on 02.04.16.
 */
public class ShuffleWordService {

    private static final int POINT = 10;

    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = new Logger(AccountServiceImpl.class);

    private final Map<String, Integer> currentScore = new HashMap<>();

    private SessionFactory sessionFactory;

    public ShuffleWordService(SessionFactory getSessionFactory){
        this.sessionFactory = getSessionFactory;
        try {
            try (BufferedReader in = new BufferedReader(new FileReader("sourse/words.txt"))) {

                String s;
                Session session = sessionFactory.openSession();
                while ((s = in.readLine()) != null) {
                    ShuffleWordDAO dao = new ShuffleWordDAO(session);
                    dao.addWord(new ShuffleWord(s));
                    logger.log(s);
                }
                session.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e ) {
            throw new RuntimeException(e);
        }
    }

    public ShuffleWord getShuffleWord(){
        final Session session = sessionFactory.openSession();
        final ShuffleWordDAO dao = new ShuffleWordDAO(session);
        ShuffleWord randWord = dao.readRandWord();
        String word = randWord.getWord();
        randWord.setWord(shuffleString(word));
        session.close();
        return randWord;
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
        final Session session = sessionFactory.openSession();
        final ShuffleWordDAO dao = new ShuffleWordDAO(session);
        final ShuffleWord word = dao.readWordById(id);
        session.close();
        return word;
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
