package hexlet.code.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UrlCheck {
    private Long      id;
    private int       statusCode;
    private String    title;
    private String    h1;
    private String    description;
    private Long      urlId;
    private Timestamp createdAt;

    public UrlCheck(int statusCode, String title, String h1, String description, Long urlId, Timestamp createdAt) {
        this.statusCode  = statusCode;
        this.title       = title;
        this.h1          = h1;
        this.description = description;
        this.urlId       = urlId;
        this.createdAt   = createdAt;
    }
}
