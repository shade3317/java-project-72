package hexlet.code.dto.url;
import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class UrlsPage extends BasePage {
    private List<Url> urls;
}
