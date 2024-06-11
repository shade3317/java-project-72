package hexlet.code;
import hexlet.code.util.NamedRoutes;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.time.LocalDateTime;
import okhttp3.Response;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
            assertThat(response.body().string()).contains("Main Page");
        });
    }

    @Test
    void testUrlsPage() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlPage() throws SQLException {
        var url = new Url("www.example.com", Timestamp.valueOf(LocalDateTime.now()));
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            var response2 = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response2.body().string()).contains("www.example.com");
        });
    }
}
