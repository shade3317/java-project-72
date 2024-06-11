@import hexlet.code.dto.BasePage
@import hexlet.code.dto.url.UrlPage
@import java.time.format.DateTimeFormatter

@param BasePage page
@param UrlPage urlPage


@template.layout.page(
    page = page,
    content = @`
        <div class="container-lg mt-5">

            <h1>Сайт ${urlPage.getUrl().getName()}</h1>

            <table class="table table-bordered table-hover mt-3">
                <tbody>
                <tr>
                    <td>ID</td>
                    <td>${urlPage.getUrl().getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${urlPage.getUrl().getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>${urlPage.getUrl().getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</td>
                </tr>
                </tbody>
            </table>
            <h2 class="mt-5">Проверки</h2>
            <form method="post" action="/urls/${urlPage.getUrl().getId()}/checks">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
            <table class="table table-bordered table-hover mt-3">
                <thead>
                    <th class="col-1">ID</th>
                    <th class="col-1">Код ответа</th>
                    <th>title</th>
                    <th>h1</th>
                    <th>description</th>
                    <th class="col-2">Дата проверки</th>
                </thead>
                <tbody>
                    @if(urlPage.getUrlChecks() != null)
                        @for(var check : urlPage.getUrlChecks())
                            <tr>
                                <td>${check.getId()}</td>
                                <td>${check.getStatusCode()}</td>
                                <td>${check.getTitle()}</td>
                                <td>${check.getH1()}</td>
                                <td>${check.getDescription()}</td>
                                <td>${check.getCreatedAt().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}</td>
                            </tr>
                        @endfor
                    @endif
                </tbody>
            </table>
        </div>
    `
)