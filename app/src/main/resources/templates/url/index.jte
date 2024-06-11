@import hexlet.code.dto.BasePage
@import hexlet.code.dto.url.UrlsPage
@import hexlet.code.util.NamedRoutes

@import java.time.format.DateTimeFormatter

@param BasePage page
@param UrlsPage urlsPage


@template.layout.page(
    page = page,
    content = @`
        <div class="container mt-5">
            <div class="row">
                <div class="container-lg mt-5">
                    <h1>Сайты</h1>
                    <table class="table table-bordered table-hover mt-3">
                        <thead>
                        <tr>
                            <th class="col-1">ID</th>
                            <th>Имя</th>
                            <th class="col-2">Последняя проверка</th>
                            <th class="col-1">Код ответа</th>
                        </tr>
                        </thead>
                        <tbody>
                            @for(var url : urlsPage.getUrls())
                                <tr>
                                    <td>${url.getId()}</td>
                                    <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                                    <td>
                                        @if(url.getUrlCheckList() != null)
                                            ${url.getLastDateCheck().toLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}
                                        @endif
                                    </td>
                                    <td>
                                        @if(url.getUrlCheckList() != null)
                                            ${url.getLastStatusCodeCheck()}
                                        @endif
                                    </td>
                                </tr>
                            @endfor
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    `
)