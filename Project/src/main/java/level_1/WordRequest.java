package level_1;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Snach on 07.04.16.
 */
public class WordRequest {
    private long id;
    @NotNull
    private String login;
    @NotNull
    private String word;

    public WordRequest() {
    }

    public WordRequest(long id, @NotNull String login, @NotNull String word) {
        this.id = id;
        this.login = login;
        this.word = word;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    @NotNull
    public String getWord() {
        return word;
    }

    public void setWord(@NotNull String word) {
        this.word = word;
    }
}
