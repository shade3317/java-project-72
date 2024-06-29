package hexlet.code;
import hexlet.code.repository.UrlRepository;
import hexlet.code.model.Url;
import hexlet.code.util.NamedRoutes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


class AppTest {
    private               Javalin       app;
    private static        MockWebServer mockWebServer;
    private static final  String        CORRECT_URL_1 = "https://ru.hexlet.io";
    private static final  String        CORRECT_URL_2 = "https://google.com";
    private static final  String        CORRECT_URL_3 = "https://github.com";
    private static final  String        INVALID_URL   = "hps:/blablaurl";
    private static        String        fixturePath   = "src/test/resources/testMock.html";
    private static        String        testBody;


    @BeforeAll
    public static void setUpMock() throws IOException {
        mockWebServer = new MockWebServer();
        testBody      = Files.readString(Paths.get(fixturePath).toAbsolutePath().normalize());
    }

    @AfterAll
    public static void shutdownMock() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public final void setUp() throws SQLException, IOException {
        app = App.getApp();
    }


    @Test
    void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());

            assertEquals(200, response.code());
        });
    }

    @Test
    void testCreateUrlPositive() {
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post(NamedRoutes.urlsPath(), "url=" + CORRECT_URL_1)) {
                assertTrue(UrlRepository.findByName(CORRECT_URL_1).isPresent());
                var body = response.body().string();

                assertEquals(200, response.code());
                assertTrue(body.contains(CORRECT_URL_1));
            }
        });
    }

    @Test
    void testCreateUrlNegative() {
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post(NamedRoutes.urlsPath(), "url=" + INVALID_URL)) {
                assertTrue(UrlRepository.findByName(INVALID_URL).isEmpty());
                var body = response.body().string();

                assertEquals(200, response.code());
                assertTrue(body.contains("Некорректный URL"));
            }
        });
    }

    @Test
    void testShowUrls() {
        JavalinTest.test(app, (server, client) -> {
            UrlRepository.save(new Url(CORRECT_URL_1));
            UrlRepository.save(new Url(CORRECT_URL_2));
            UrlRepository.save(new Url(CORRECT_URL_3));

            var response = client.get(NamedRoutes.urlsPath());
            var body     = response.body().string();

            assertEquals(200, response.code());
            assertTrue(body.contains(CORRECT_URL_1));
            assertTrue(body.contains(CORRECT_URL_2));
            assertTrue(body.contains(CORRECT_URL_3));
        });
    }

    @Test
    void testShowUrlPositive() {
        JavalinTest.test(app, (server, client) -> {
            var entity = new Url(CORRECT_URL_3);
            UrlRepository.save(entity);
            assertTrue(UrlRepository.findByName(CORRECT_URL_3).isPresent());

            var response1 = client.get(NamedRoutes.urlPath(entity.getId()));
            var body      = response1.body().string();
            assertEquals(200, response1.code());
            assertTrue(body.contains(CORRECT_URL_3));
        });
    }

    @Test
    void testShowUrlNegative() {
        JavalinTest.test(app, (server, client) -> {
            var entity = new Url(CORRECT_URL_3);
            UrlRepository.save(entity);
            assertTrue(UrlRepository.findByName(CORRECT_URL_3).isPresent());

            var response2 = client.get(NamedRoutes.urlPath("42"));
            assertEquals(404, response2.code());
        });
    }

    @Test
    void testCheckUrl() throws IOException {
        MockResponse response = new MockResponse().setResponseCode(200).setBody(testBody);
        mockWebServer.enqueue(response);
        mockWebServer.start();

        JavalinTest.test(app, ((server, client) -> {
            var entity = new Url(mockWebServer.url("/").toString());

            UrlRepository.save(entity);
            assertTrue(UrlRepository.findById(entity.getId()).isPresent());

            try (var req = client.post(NamedRoutes.urlCheckPath(entity.getId()), "url=" + entity.getName())) {
                var body = req.body().string();

                assertTrue(body.contains("test"));
                assertTrue(body.contains("200"));
            }
        }));
    }
}

