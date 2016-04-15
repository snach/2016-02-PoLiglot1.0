package account;

import main.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * created by snach
 */
public class AccountServiceImpl implements AccountService {

    private final Map<String, UserProfile> sessions = new HashMap<>();

    SessionFactory sessionFactory;

    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = new Logger(AccountServiceImpl.class);

    public AccountServiceImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<UserProfile> getAllUsers() {
        final Session session = sessionFactory.openSession();
        final UserProfileDAO dao = new UserProfileDAO(session);
        final List<UserProfile> users = dao.readAll();
        session.close();
        return users;
    }

    @Override
    public boolean addUser(UserProfile user) {
        final boolean status;
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            if (dao.addUser(user)) {
                status = true;
                logger.log("Пользователь добавлен: {" + String.valueOf(user.getUserID()) + ", " + String.valueOf(user.getLogin())
                        + ", " + String.valueOf(String.valueOf(user.getPassword())) + ", " + String.valueOf(user.getEmail()) + "}\n");
            }
            else {
                status = false;
                logger.log("Пользователь НЕ добавлен");
            }
            transaction.commit();

        }

        return status;
    }

    @Override
    public void editUser(@NotNull UserProfile oldUser, UserProfile newUser) {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            dao.editUser(oldUser,newUser);
            transaction.commit();
            logger.log("Пользователь изменен: {" + String.valueOf(oldUser.getUserID()) + '}');
        }
    }

    @Override
    public void deleteUser(long userID) {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            dao.deleteUser(userID);
            transaction.commit();
            logger.log("Пользователь удален: {" + String.valueOf(userID)  + '}');
        }
    }

    @Override
    @Nullable
    public UserProfile getUserByID(long userID) {
        final Session session = sessionFactory.openSession();
        final UserProfileDAO dao = new UserProfileDAO(session);
        final UserProfile user = dao.readUserByID(userID);
        session.close();
        return user;

    }

    @Override
    @Nullable
    public UserProfile getUserByLogin(String login) {
        final Session session = sessionFactory.openSession();
        final UserProfileDAO dao = new UserProfileDAO(session);
        final UserProfile user = dao.readUserByLogin(login);
        session.close();
        return user;
    }

    @Override
    @Nullable
    public UserProfile getUserByEmail(String email) {
        final Session session = sessionFactory.openSession();
        final UserProfileDAO dao = new UserProfileDAO(session);
        final UserProfile user = dao.readUserByEmail(email);
        session.close();
        return user;
    }

    @Override
    @Nullable
    public UserProfile getUserBySession(@Nullable String sessionID) {
        if (sessions.get(sessionID) != null) {
            return sessions.get(sessionID);
        } else {
            return null;
        }
    }

    @Override
    public boolean isLoggedIn(String sessionID) {
        return sessions.containsKey(sessionID);
    }

    @Override
    public void addSession(String sessionID, UserProfile user) {
        sessions.put(sessionID, user);
        logger.log("Сессия добавлена: {" + String.valueOf(user.getUserID()) + ", " + String.valueOf(user.getLogin())
                + ", " + String.valueOf(String.valueOf(user.getPassword())) + ", " + String.valueOf(user.getEmail()) + '}');
    }

    @Override
    public void deleteSession(String sessionID) {
        final UserProfile user = getUserBySession(sessionID);
        if (user != null) {
            sessions.remove(sessionID);
            logger.log("Сессия удалена: {" + String.valueOf(user.getUserID()) + ", " + String.valueOf(user.getLogin())
                    + ", " + String.valueOf(String.valueOf(user.getPassword())) + ", " + String.valueOf(user.getEmail()) + '}');
        } else {
            logger.log("Сессия не может быть удалена, тк не добавлена");
        }
    }

    @Override
    public boolean checkAuth(@NotNull String userName, @NotNull String password){
        final UserProfile user = getUserByLogin(userName);
        if (user!= null) {
            return user.getPassword().equals(password);
        } else {
            logger.log("Юзер " + userName + " не найден");
            return false;
        }
    }

    @Override
    public void editScore(String login, int newScore) {
        final Session session = sessionFactory.openSession();
        final UserProfileDAO dao = new UserProfileDAO(session);
        dao.editUserScore(login,newScore);
        session.close();
    }
}

