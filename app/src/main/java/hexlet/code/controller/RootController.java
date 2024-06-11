package hexlet.code.controller;
import hexlet.code.dto.url.BuildUrlPage;

import java.util.Collections;

import io.javalin.http.Context;


public final class RootController {
    public static void show(Context context) {
        BuildUrlPage page = new BuildUrlPage();
        page.setFlash(context.consumeSessionAttribute("flash"));
        page.setFlashType(context.consumeSessionAttribute("flashType"));
        context.render("index.jte", Collections.singletonMap("page", page));
    }
}
