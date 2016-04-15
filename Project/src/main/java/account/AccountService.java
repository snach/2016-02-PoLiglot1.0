package account;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by Snach on 26.03.16.
 */
public interface AccountService {

    Collection<UserProfile> getAllUsers();

    boolean addUser(UserProfile user);

    UserProfile getUserByID(long userID);

    UserProfile getUserByLogin(String login);

    UserProfile getUserByEmail(String email);

    UserProfile getUserBySession(@Nullable String sessionID);

    boolean isLoggedIn(String sessionID);

    void addSession(String sessionID, UserProfile user);

    void deleteSession(String sessionID);

    void editUser(@NotNull UserProfile oldUser, UserProfile newUser);

    void deleteUser(long userID);

    boolean checkAuth(@NotNull String userName, @NotNull String password);

    void editScore(String login, int newScore);
}
