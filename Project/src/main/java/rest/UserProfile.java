package rest;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author esin88
 */
public class UserProfile {
    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);
    @NotNull
    private long userID;
    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private String email;

    public UserProfile() {
        login = "";
        password = "";
    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email) {
        this.userID = ID_GENETATOR.getAndIncrement();
        this.login = login;
        this.password = password;
        this.email = email;
    }

    @NotNull
    public long getUserID() {
        return userID;
    }

    public void setUserID() {
        this.userID = ID_GENETATOR.getAndIncrement();
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    //@NotNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }


}
