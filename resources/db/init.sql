CREATE TABLE titles(
        id              SERIAL      NOT NULL    PRIMARY KEY,
        link            TEXT        NOT NULL,
        name            TEXT        NOT NULL,
        original_name   TEXT        NOT NULL,
        watched_series  INTEGER     NOT NULL,
        total_series    INTEGER     NOT NULL,
        status          INTEGER     NOT NULL
);

CREATE TABLE users(
    id                  SERIAL      NOT NULL    PRIMARY KEY,
    name                TEXT        NOT NULL    UNIQUE
);

CREATE TABLE titles_2_users(
        title_id        INTEGER     NOT NULL    REFERENCES titles(id),
        user_id         INTEGER     NOT NULL    REFERENCES users(id)
);