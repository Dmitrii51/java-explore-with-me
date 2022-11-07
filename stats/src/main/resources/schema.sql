CREATE TABLE IF NOT EXISTS stats (
    id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app varchar NOT NULL,
    uri varchar NOT NULL,
    ip varchar NOT NULL,
    timestamp timestamp NOT NULL,
    CONSTRAINT pk_stats PRIMARY KEY (id)
);