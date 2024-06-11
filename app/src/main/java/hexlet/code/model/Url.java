package hexlet.code.model;

import java.sql.Timestamp;
import lombok.NonNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@RequiredArgsConstructor
public class Url {
    private Long id;
    @NonNull private String name;
    @NonNull private Timestamp createdAt;
}
