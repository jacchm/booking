package com.jacchm.project.config.database;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.lang.NonNull;

@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {

  public static final String DATABASE_NAME = "project-webapp";

  @Bean
  public MongoClient mongoClient() {
    return MongoClients.create();
  }

  @NonNull
  @Override
  protected String getDatabaseName() {
    return DATABASE_NAME;
  }

}