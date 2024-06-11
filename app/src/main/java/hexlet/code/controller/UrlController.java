package hexlet.code.controller;
import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Utilities;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;


public class UrlController implements CrudHandler {
    /**
     * @param context
     */
    @Override
    public void create(@NotNull Context context) {
        String string = context.formParam("url");
        try {
            URI uri       = new URL(string).toURI();
            String name   = Utilities.uriToString(uri);
            boolean check = UrlRepository.find(name).isPresent();
            if (check) {
                context.sessionAttribute("flash", "Страница уже существует");
                context.sessionAttribute("flashType", "danger");
                context.redirect(NamedRoutes.ROOT_PATH);
                return;
            }
            Url urlToSave = new Url(name, Timestamp.valueOf(LocalDateTime.now()));
            UrlRepository.save(urlToSave);
            context.sessionAttribute("flash", "Страница успешно добавлена");
            context.sessionAttribute("flashType", "success");
            context.redirect(NamedRoutes.ROOT_PATH);
        } catch (URISyntaxException | MalformedURLException e) {
            context.sessionAttribute("flash", "Некорректный URL");
            context.sessionAttribute("flashType", "danger");
            context.redirect(NamedRoutes.ROOT_PATH);
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
    }

    /**
     * @param context
     * @param s
     */
    @Override
    public void delete(@NotNull Context context, @NotNull String s) {
    }

    /**
     * @param context
     */
    @Override
    public void getAll(@NotNull Context context) {
        List<Url> urls = null;
        try {
            urls = UrlRepository.getEntities();
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
        var page = new UrlsPage(urls);
        context.render("url/index.jte", Collections.singletonMap("page", page));

    }

    /**
     * @param context
     * @param s
     */
    @Override
    public void getOne(@NotNull Context context, @NotNull String s) {
        Long id = context.pathParamAsClass("url-id", Long.class).get();
        Url url = null;
        try {
            url = UrlRepository.find(id).orElseThrow(() -> new NotFoundResponse("Url with ID: " + id + " not found"));
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }
        UrlPage page = new UrlPage(url);
        context.render("url/show.jte", Collections.singletonMap("page", page));
    }

    /**
     * @param context
     * @param s
     */
    @Override
    public void update(@NotNull Context context, @NotNull String s) {
    }
}
