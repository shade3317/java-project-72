package hexlet.code.repository;

import lombok.Getter;
import lombok.Setter;

import com.zaxxer.hikari.HikariDataSource;


@Getter
@Setter
public class BaseRepository {
    public static HikariDataSource dataSource;
}
