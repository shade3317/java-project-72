package hexlet.code;
import hexlet.code.controller.RootController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.controller.UrlController;
import hexlet.code.util.Utilities;

import java.io.IOException;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class App {
    public static void main(String[] args) throws SQLException, IOException {
        getApp().start(Utilities.getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
        String databaseUrl = System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
        String databaseUsername = System.getenv()
                .getOrDefault("JDBC_DATABASE_USERNAME", null);
        String databasePassword = System.getenv()
                .getOrDefault("JDBC_DATABASE_PASSWORD", null);

        var hikariConfig = new HikariConfig();
        hikariConfig.setUsername(databaseUsername);
        hikariConfig.setPassword(databasePassword);
        hikariConfig.setJdbcUrl(databaseUrl);


        var dataSource = new HikariDataSource(hikariConfig);
        var sql = Utilities.readResourceFile("schema.sql");

        log.info(sql);

        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            if (databaseUrl.contains("h2:mem")) {
                config.bundledPlugins.enableDevLogging();
            }
            config.fileRenderer(new JavalinJte(Utilities.createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        app.post(NamedRoutes.urlCheckPath("{id}"), UrlController::check);

        return app;
    }


}

