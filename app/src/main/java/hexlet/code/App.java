package hexlet.code;
import hexlet.code.controller.RootController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.controller.UrlController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
//import java.sql.DriverManager;
//import org.postgresql.Driver;
import java.sql.SQLException;
import java.util.stream.Collectors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class App {
    public static void main(String[] args) throws SQLException, IOException {
        getApp().start(getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
        //DriverManager.registerDriver(new Driver());
        //DriverManager.drivers().forEach(d -> log.info(d.toString()));

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
        var sql = readResourceFile("schema.sql");

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
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        app.post(NamedRoutes.urlCheckPath("{id}"), UrlController::check);

        return app;
    }

    static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }


    static TemplateEngine createTemplateEngine() {
        ClassLoader          classLoader  = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);

        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}

