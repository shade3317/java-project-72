DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    ID SERIAL PRIMARY KEY,
    NAME VARCHAR(255),
    CREATED_AT TIMESTAMP
);