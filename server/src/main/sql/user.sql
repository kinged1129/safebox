CREATE TABLE IF NOT EXISTS user(
    id              VARCHAR(36)     NOT NULL,
    name            VARCHAR(50)         NULL,
    email           VARCHAR(50)     NOT NULL,
    password        VARCHAR(99)     NOT NULL,
    creation        TIMESTAMP       NOT NULL,
    PRIMARY KEY (id)
);