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


public class UrlCheckController extends BaseRepository {
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
