package hexlet.code.repository;
import hexlet.code.model.UrlCheck;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UrlCheckRepository extends BaseRepository {
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

    public static Map<Long, UrlCheck> findLastChecks() {
        var sql = "SELECT uc.url_id, uc.status_code, uc.created_at "
                + "FROM url_checks uc "
                + "JOIN ( "
                + "    SELECT url_id, MAX(created_at) AS max_created_at"
                + "    FROM url_checks "
                + "    GROUP BY url_id "
                + ") uc_max ON uc.url_id = uc_max.url_id AND uc.created_at = uc_max.max_created_at;";
        var result = new HashMap<Long, UrlCheck>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                var urlCheck = UrlCheck.builder()
                        .createdAt(resultSet.getTimestamp("created_at"))
                        .urlId(resultSet.getLong("url_id"))
                        .statusCode(resultSet.getInt("status_code"))
                        .build();
                result.put(urlCheck.getUrlId(), urlCheck);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
