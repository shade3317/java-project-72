package hexlet.code;
import hexlet.code.util.NamedRoutes;

import okhttp3.Response;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


public final class AppTest {
    private Javalin app;

    @BeforeEach
    public void setUp() throws SQLException {
        app = App.getApp();
    }

    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            Response response = client.get(NamedRoutes.ROOT_PATH);
            assertThat(response.code()).isEqualTo(200);
            assert response.body() != null;

        });
    }

    @Test
    public void testGetPort() {
        String defaultPort = String.valueOf(App.getPort());
        assertEquals("7070", defaultPort);
    }

    @Test
    public void testGetDatabaseUrl() {
        String defaultUrl = App.getDatabaseUrl();
        assertEquals("jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;", defaultUrl);
    }
}
