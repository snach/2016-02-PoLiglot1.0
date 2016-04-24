package account;

import base.AccountService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * created by snach
 */
public class AccountServiceImpl implements AccountService {

    private final Map<String, UserProfile> sessions = new HashMap<>();

    final SessionFactory sessionFactory;

    private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);

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
    public List<UserProfile> getTopUsers() {
        final Session session = sessionFactory.openSession();
        final UserProfileDAO dao = new UserProfileDAO(session);
        final List<UserProfile> users = dao.readTop();
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
                LOGGER.info("Пользователь добавлен: {" + String.valueOf(user.getUserID()) + ", " + String.valueOf(user.getLogin())
                        + ", " + String.valueOf(String.valueOf(user.getPassword())) + ", " + String.valueOf(user.getEmail()) + '}');
            }
            else {
                status = false;
                LOGGER.info("Пользователь НЕ добавлен");
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
            LOGGER.info("Пользователь изменен: {" + String.valueOf(oldUser.getUserID()) + '}');
        }
    }

    @Override
    public void deleteUser(long userID) {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            dao.deleteUser(userID);
            transaction.commit();
            LOGGER.info("Пользователь удален: {" + String.valueOf(userID)  + '}');
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
        LOGGER.info("Сессия добавлена: {" + String.valueOf(user.getUserID()) + ", " + String.valueOf(user.getLogin())
                + ", " + String.valueOf(String.valueOf(user.getPassword())) + ", " + String.valueOf(user.getEmail()) + '}');
    }

    @Override
    public void deleteSession(String sessionID) {
        final UserProfile user = getUserBySession(sessionID);
        if (user != null) {
            sessions.remove(sessionID);
            LOGGER.info("Сессия удалена: {" + String.valueOf(user.getUserID()) + ", " + String.valueOf(user.getLogin())
                    + ", " + String.valueOf(String.valueOf(user.getPassword())) + ", " + String.valueOf(user.getEmail()) + '}');
        } else {
            LOGGER.info("Сессия не может быть удалена, тк не добавлена");
        }
    }

    @Override
    public boolean checkAuth(@NotNull String userName, @NotNull String password){
        final UserProfile user = getUserByLogin(userName);
        if (user!= null) {
            return user.getPassword().equals(password);
        } else {
            LOGGER.info("Юзер " + userName + " не найден");
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

