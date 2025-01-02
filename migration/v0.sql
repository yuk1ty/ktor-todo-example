CREATE TABLE todo (
    todo_id UUID PRIMARY KEY,
    title VARCHAR(1024) NOT NULL,
    description VARCHAR(2048),
    due TIMESTAMP,
    status VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);