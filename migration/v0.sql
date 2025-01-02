CREATE TABLE todo (
    todo_id UUID PRIMARY_KEY,
    title VARCHAR(1024) NOT NULL,
    description VARCHAR(2048),
    due TIMESTAMP,
    status VARCHAR(32) NOT NULL,
    created TIMESTAMP NOT NULL,
    updated TIMESTAMP NOT NULL
);