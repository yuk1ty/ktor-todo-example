-- Configuration for application

CREATE DATABASE todo_app_kt;

\c todo_app_kt;

CREATE ROLE readonly WITH LOGIN PASSWORD 'password';
ALTER ROLE readonly WITH LOGIN;
GRANT CONNECT ON DATABASE todo_app_kt TO readonly;
GRANT USAGE ON SCHEMA public TO readonly;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly;

CREATE ROLE writable WITH LOGIN PASSWORD 'password';
ALTER ROLE writable WITH LOGIN;
GRANT CONNECT ON DATABASE todo_app_kt TO writable;
GRANT USAGE ON SCHEMA public TO writable;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO writable;

-- Configuration for tests

CREATE DATABASE todo_app_kt_test;

\c todo_app_kt_test;

CREATE ROLE readonly WITH LOGIN PASSWORD 'password';
ALTER ROLE readonly WITH LOGIN;
GRANT CONNECT ON DATABASE todo_app_kt_test TO readonly;
GRANT USAGE ON SCHEMA public TO readonly;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO readonly;

CREATE ROLE writable WITH LOGIN PASSWORD 'password';
ALTER ROLE writable WITH LOGIN;
GRANT CONNECT ON DATABASE todo_app_kt_test TO writable;
GRANT USAGE ON SCHEMA public TO writable;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO writable;
