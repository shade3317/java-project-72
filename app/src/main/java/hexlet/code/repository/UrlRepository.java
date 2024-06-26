package hexlet.code.repository;
import hexlet.code.model.Url;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            var timestamp     = new Timestamp(System.currentTimeMillis());
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Произошла ошибка при извлечении идентификтора");
            }
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql    = "SELECT * FROM urls";
        var result = new ArrayList<Url>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                var id        = resultSet.getLong("id");
                var name      = resultSet.getString("name");
                var timestamp = resultSet.getTimestamp("created_at");
                var url       = new Url(name, timestamp);
                url.setId(id);
                result.add(url);
            }
        }

        return result.stream().collect(Collectors.toList());
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var name      = resultSet.getString("name");
                var timestamp = resultSet.getTimestamp("created_at");
                var url       = new Url(name, timestamp);
                url.setId(id);
                return Optional.of(url);
            }
        }

        return Optional.empty();
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                var id        = resultSet.getLong("id");
                var timestamp = resultSet.getTimestamp("created_at");
                var url       = new Url(name, timestamp);
                url.setId(id);
                return Optional.of(url);
            }
        }

        return Optional.empty();
    }
}
