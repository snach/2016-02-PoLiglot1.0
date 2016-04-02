package level_1;

import org.hibernate.Session;

/**
 * Created by Snach on 02.04.16.
 */
public class ShuffleWordDAO {
    private Session session;
    public ShuffleWordDAO(Session session) {
        this.session = session;
    }

    public boolean addWord(ShuffleWord word) {
        session.save(word);
        return true;
    }
}
