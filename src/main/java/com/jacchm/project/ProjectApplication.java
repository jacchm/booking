package com.jacchm.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.time.Duration;
import java.time.Instant;

@EnableReactiveMongoAuditing
@EnableWebFlux
@EnableR2dbcRepositories
@SpringBootApplication
public class ProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProjectApplication.class, args);
    System.out.println("Programming is awesome!");
    System.out.println("Instant = " + Instant.now().toString());
    System.out.println("Duration = " + Duration.ofMinutes(55));
  }

}
