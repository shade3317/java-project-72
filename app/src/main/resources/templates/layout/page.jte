@import hexlet.code.util.Routes
@import gg.jte.Content
@param Content content


<!doctype html>
<html lang="ru">
    <head>
        <meta charset="utf-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link href="${Routes.BOOTSTRAP_LINK}" rel="stylesheet" integrity="${Routes.BOOTSTRAP_INTEGRITY}" crossorigin="anonymous">
        <title>Project_4</title>
    </head>
    <body>
        <div>
            ${content}
        </div>
    </body>
</html>