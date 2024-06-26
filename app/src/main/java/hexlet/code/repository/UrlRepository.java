package hexlet.code.repository;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

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

    public static void saveCheck(UrlCheck urlCheck) throws SQLException {
        var sql = "INSERT INTO url_checks (url_id, status_code, title, h1, description, created_at) "
                + "values (?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            var createdAt = new Timestamp(System.currentTimeMillis());
            preparedStatement.setLong(1, urlCheck.getUrlId());
            preparedStatement.setInt(2, urlCheck.getStatusCode());
            preparedStatement.setString(3, urlCheck.getTitle());
            preparedStatement.setString(4, urlCheck.getH1());
            preparedStatement.setString(5, urlCheck.getDescription());
            preparedStatement.setTimestamp(6, createdAt);
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Произошла ошибка при извлечении идентификтора");
            }
        }
    }

    public static List<UrlCheck> findChecksById(Long urlId) throws SQLException {
        var sql    = "SELECT * FROM url_checks where url_id = ? ORDER BY created_at DESC";
        var result = new ArrayList<UrlCheck>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, urlId);
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                var id          = resultSet.getLong("id");
                var statusCode  = resultSet.getInt("status_code");
                var created     = resultSet.getTimestamp("created_at");
                var title       = resultSet.getString("title");
                var h1          = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var urlChecks   = new UrlCheck(statusCode, title, h1, description, id, created);
                urlChecks.setId(id);
                result.add(urlChecks);
            }
        }

        return result;
    }

    public static List<Url> findLastCheck(List<Url> urls) throws SQLException {
        var sql        = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        var listChecks = new ArrayList<UrlCheck>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (var url : urls) {
                statement.setLong(1, url.getId());
                var resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    var statusCode = resultSet.getInt("status_code");
                    var created    = resultSet.getTimestamp("created_at");
                    listChecks.add(new UrlCheck(statusCode, created));
                    url.setLastStatusCodeCheck(statusCode);
                    url.setLastDateCheck(created);
                    url.setUrlCheckList(listChecks);
                }
            }
        }

        return urls;
    }
}
