package account;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * created by snach
 */
@Entity
@Table(name = "users")
public class UserProfile implements Serializable { // Serializable Important to Hibernate!
    private static final long serialVersionUID = -8706689714326132798L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private long id;

    @NotNull
    @Column(name = "login")
    private String login;

    @NotNull
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @NotNull
    @Column(name = "score", columnDefinition = "int default 0")
    private Integer score;

    public UserProfile() {
        login = "";
        password = "";
        email = "";
        score = 0;
    }

    public UserProfile(@NotNull String login, @NotNull String password, @NotNull String email) {
        this.id = -1;
        this.login = login;
        this.password = password;
        this.email = email;
        this.score = 0;
    }

    public long getUserID() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    @NotNull
    public Integer  getScore() {
        return score;
    }

    public void  setScore(int score) {
        this.score = score;
    }


}