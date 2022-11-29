create TABLE IF NOT EXISTS users (
    user_id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name varchar NOT NULL,
    email varchar NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (user_id),
    CONSTRAINT unique_user_email UNIQUE (email)
);

create TABLE IF NOT EXISTS categories (
    category_id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name varchar NOT NULL,
    CONSTRAINT unique_category_name UNIQUE (name)
);

create TABLE IF NOT EXISTS locations (
    location_id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    latitude real NOT NULL,
    longitude real NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (location_id)
);

create TABLE IF NOT EXISTS events (
    event_id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation varchar NOT NULL,
    created timestamp NOT NULL,
    description varchar NOT NULL,
    event_date timestamp NOT NULL,
    paid boolean NOT NULL,
    participant_limit  integer NOT NULL,
    published timestamp,
    request_moderation boolean NOT NULL,
    state varchar NOT NULL,
    title varchar NOT NULL,
    category_id bigint REFERENCES categories (category_id),
    initiator_id bigint REFERENCES users (user_id),
    location_id bigint REFERENCES locations (location_id),
    CONSTRAINT pk_event PRIMARY KEY (event_id)
);

create TABLE IF NOT EXISTS requests (
    request_id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id bigint REFERENCES events (event_id),
    created timestamp NOT NULL,
    requester_id bigint REFERENCES users (user_id),
    status NOT NULL,
);

create TABLE IF NOT EXISTS compilations (
    compilation_id bigint GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned boolean NOT NULL,
    title varchar NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (compilation_id)
);
