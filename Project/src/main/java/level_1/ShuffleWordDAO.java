package level_1;

import account.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import java.lang.Object;

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

    public ShuffleWord readRandWord() {
        Criteria criteria1 = session.createCriteria(ShuffleWord.class);

        criteria1.setProjection(Projections.rowCount());
        long rowCount = (Long)criteria1.uniqueResult();
        long randId = 1 + (long)(Math.random() * ((rowCount - 1) + 1));
        Criteria criteria2 = session.createCriteria(ShuffleWord.class);
        return (ShuffleWord) criteria2.add(Restrictions.eq("id", randId)).uniqueResult();
    }
}