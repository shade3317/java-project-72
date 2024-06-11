package hexlet.code;
import hexlet.code.controller.RootController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.controller.UrlController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import static io.javalin.apibuilder.ApiBuilder.crud;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;


public class App {
    public static void main(String[] args) throws SQLException {
        Javalin app = getApp();
        app.start(getPort());
    }

    public static Javalin getApp() throws SQLException {
        HikariConfig     hikariConfig   = new HikariConfig();
        hikariConfig.setJdbcUrl(getDatabaseUrl());
        HikariDataSource dataSource     = new HikariDataSource(hikariConfig);
        InputStream      inputStream    = App.class.getClassLoader().getResourceAsStream("schema.sql");

        String sql = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        Javalin app = Javalin.create(config -> config.plugins.enableDevLogging());
        JavalinJte.init(createTemplateEngine());
        setRoutes(app);

        return app;
    }

    static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");

        return Integer.parseInt(port);
    }

    static String getDatabaseUrl() {
        return System.getenv()
                .getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;");
    }

    static TemplateEngine createTemplateEngine() {
        ClassLoader          classLoader  = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);

        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    static void setRoutes(Javalin app) {
        app.get(NamedRoutes.ROOT_PATH, RootController::show);

        app.routes(() -> {
            crud("urls/{url-id}", new UrlController());
        });
    }
}

