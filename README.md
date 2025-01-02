# ktor-todo-example

An example application to illustrate how to build a simple RESTful API with Ktor, with mixing of the functional programming style and the object-oriented programming style.

## Getting Started

### Launch database server

```
cd docker
docker-compose up -d
```

### Run `sqldef`

> [!NOTE]
> You need to install `sqldef` in advance before running the following commands. Please see https://github.com/sqldef/sqldef.

```
psqldef -U postgres -W password todo_app_kt < migration/v0.sql
```

### Run the application

```
./gradlew run
```

### Check if the server runs

```
curl http://localhost:8080/health
```