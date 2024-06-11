package hexlet.code.model;

import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Url {
    private Long           id;
    private String         name;
    private Timestamp      createdAt;
    private List<UrlCheck> urlCheckList;
    private Timestamp      lastDateCheck;
    private int            lastStatusCodeCheck;

    public Url(String name, Timestamp createdAt) {
        this.name      = name;
        this.createdAt = createdAt;
    }

    public Url(String name) {
        this.name = name;
    }
}
