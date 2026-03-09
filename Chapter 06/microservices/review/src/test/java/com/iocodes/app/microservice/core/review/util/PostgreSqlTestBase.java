package com.iocodes.app.microservice.core.review.util;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class PostgreSqlTestBase {

    static DockerImageName databaseImage = DockerImageName.parse("postgres:16.3-alpine");

    @ServiceConnection
    protected static PostgreSQLContainer database = new PostgreSQLContainer(databaseImage).withStartupTimeoutSeconds(300);

    static {
        database.start();
    }

}
