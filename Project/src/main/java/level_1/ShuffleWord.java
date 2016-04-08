package level_1;

import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Snach on 02.04.16.
 */
@Entity
@Table(name = "words")
public class ShuffleWord implements Serializable {
    private static final long serialVersionUID = -8706689714326132798L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "word")
    private String word;

    public ShuffleWord(@NotNull String word) {
        this.id = -1;
        this.word = word;

    }
    public ShuffleWord() {
        word = "";

    }

    public void setWord(@NotNull String word) {
        this.word = word;
    }
    @NotNull
    public String getWord() {
        return word;
    }

    public long getId() {
        return id;
    }
}
