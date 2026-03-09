package com.iocodes.app.microservice.core.recommendation.util;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public abstract class MongoDbTestBase {

    static DockerImageName databaseImage = DockerImageName.parse("mongo:6.0.4");

    @ServiceConnection
    private static MongoDBContainer database = new MongoDBContainer(databaseImage).withStartupTimeout(Duration.ofSeconds(300));

    static {
        database.start();
    }

}
