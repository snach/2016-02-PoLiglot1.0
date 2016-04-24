package game.firstlvl;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Snach on 07.04.16.
 */
public class ShuffleWord {
    private long id;

    @NotNull
    private String word;

    public ShuffleWord(long id,  @NotNull String word) {
        this.id = id;
        this.word = word;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NotNull
    public String getWord() {
        return word;
    }

    public void setWord(@NotNull String word) {
        this.word = word;
    }
}
