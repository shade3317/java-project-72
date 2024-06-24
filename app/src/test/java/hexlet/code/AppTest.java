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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class AppTest {
    private        Javalin       app;
    private static MockWebServer mockWebServer;
    private final  String        correctUrl1 = "https://ru.hexlet.io";
    private final  String        correctUrl2 = "https://google.com";
    private final  String        correctUrl3 = "https://github.com";
    private final  String        invalidUrl  = "hps:/blablaurl";
    private static String        fixturePath = "src/test/resources/testMock.html";
    private static String        testBody;


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

            Assertions.assertEquals(200, response.code());
        });
    }

    @Test
    void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            try (var response = client.post(NamedRoutes.urlsPath(), "url=" + correctUrl1)) {
                Assertions.assertTrue(UrlRepository.findByName(correctUrl1).isPresent());
                var body = response.body().string();

                Assertions.assertEquals(200, response.code());
                Assertions.assertTrue(body.contains(correctUrl1));
            }
            try (var response = client.post(NamedRoutes.urlsPath(), "url=" + invalidUrl)) {
                Assertions.assertTrue(UrlRepository.findByName(invalidUrl).isEmpty());
                var body = response.body().string();

                Assertions.assertEquals(200, response.code());
                Assertions.assertTrue(body.contains("Некорректный URL"));
            }
        });
    }

    @Test
    void testShowUrls() {
        JavalinTest.test(app, (server, client) -> {
            UrlRepository.save(new Url(correctUrl1));
            UrlRepository.save(new Url(correctUrl2));
            UrlRepository.save(new Url(correctUrl3));

            Assertions.assertTrue(UrlRepository.findByName(correctUrl1).isPresent());
            Assertions.assertTrue(UrlRepository.findByName(correctUrl2).isPresent());
            Assertions.assertTrue(UrlRepository.findByName(correctUrl3).isPresent());

            var response = client.get(NamedRoutes.urlsPath());
            var body     = response.body().string();

            Assertions.assertEquals(200, response.code());
            Assertions.assertTrue(body.contains(correctUrl1));
            Assertions.assertTrue(body.contains(correctUrl2));
            Assertions.assertTrue(body.contains(correctUrl3));
        });
    }

    @Test
    void testShowUrl() {
        JavalinTest.test(app, (server, client) -> {
            var entity = new Url(correctUrl3);
            UrlRepository.save(entity);
            Assertions.assertTrue(UrlRepository.findByName(correctUrl3).isPresent());

            var response1 = client.get(NamedRoutes.urlPath(entity.getId()));
            var body      = response1.body().string();
            Assertions.assertEquals(200, response1.code());
            Assertions.assertTrue(body.contains(correctUrl3));

            var response2 = client.get(NamedRoutes.urlPath("42"));
            Assertions.assertEquals(404, response2.code());
        });
    }

    @Test
    void testCheckUrl() throws IOException {
        MockResponse response = new MockResponse().setResponseCode(302).setBody(testBody);
        mockWebServer.enqueue(response);
        mockWebServer.enqueue(response);
        mockWebServer.start();

        JavalinTest.test(app, ((server, client) -> {
            var entity = new Url(mockWebServer.url("/").toString());

            UrlRepository.save(entity);
            Assertions.assertTrue(UrlRepository.findById(entity.getId()).isPresent());

            try (var req = client.post(NamedRoutes.urlCheckPath(entity.getId()), "url=" + entity.getName())) {
                var body = req.body().string();

                Assertions.assertTrue(body.contains("test"));
                Assertions.assertTrue(body.contains("302"));
            }
            try (var req = client.post(NamedRoutes.urlCheckPath(entity.getId()))) {
                Assertions.assertTrue(UrlRepository.findById(entity.getId()).isPresent());
                var body = req.body().string();

                Assertions.assertTrue(body.contains("test"));
                Assertions.assertTrue(body.contains("302"));
            }
        }));
    }
}

