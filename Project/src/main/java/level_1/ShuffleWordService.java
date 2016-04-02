package level_1;

import main.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import main.AccountServiceImpl;

/**
 * Created by Snach on 02.04.16.
 */
public class ShuffleWordService {

    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = new Logger(AccountServiceImpl.class);
    private SessionFactory sessionFactory;

    public ShuffleWordService(SessionFactory getSessionFactory){
        this.sessionFactory = getSessionFactory;
        try {
            try (BufferedReader in = new BufferedReader(new FileReader("sourse/words.txt"))) {
                //В цикле построчно считываем файл
                String s;
                Session session = sessionFactory.openSession();
                while ((s = in.readLine()) != null) {
                    ShuffleWordDAO dao = new ShuffleWordDAO(session);
                    dao.addWord(new ShuffleWord(s));
                    logger.log(s);
                }
                session.close();
            }
            //Также не забываем закрыть файл

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e ) {
            throw new RuntimeException(e);
        }


    }
}
