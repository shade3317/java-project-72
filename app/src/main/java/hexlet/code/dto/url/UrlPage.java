package hexlet.code.dto.url;
import hexlet.code.dto.BasePage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;

import java.util.List;
import lombok.Getter;


@Getter
public class UrlPage extends BasePage {
    private Url            url;
    private List<UrlCheck> urlChecks;

    public UrlPage(Url url) {
        this.url = url;
    }
    public UrlPage(Url url, List<UrlCheck> urlChecks) {
        this.url       = url;
        this.urlChecks = urlChecks;
    }
}
