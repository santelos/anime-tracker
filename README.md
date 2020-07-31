# anime-tracker
Simple tracker for anime that currently watched

# Запуски сервера
Конфигурация сервера происходит параметрами среды:
- PORT - порт сервера (8080 по умолчанию)
- JDBC_DATABASE_URL или DATABASE_URL - URL БД (jdbc:postgresql://localhost:5432/postgres по умолчанию)
- JDBC_DATABASE_USER - Пользователь БД (postgres по умолчанию)
- JDBC_DATABASE_PASSWORD - Пароль пользователя БД (postgres по умолчанию)

TODO:
- Server Side Rendering
- Отображение тайтлов:
    1) Картинка
    2) Название / Оригинальное название
    3) Кол-во просмотренных / Общее кол-во серий
    4) Люди
    5) Ссылка на мал
    6) Статус (plan to watch - 0, watching - 1, hold - 2, dropped - 3, completed - 4)

- в edit-title.html правильно выставлять выделения текущих смотрящих
- редактирование картинки
- количество серий удобным +
- роут на css