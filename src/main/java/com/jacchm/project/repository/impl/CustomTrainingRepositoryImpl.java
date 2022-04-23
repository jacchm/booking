package com.jacchm.project.repository.impl;

import com.jacchm.project.model.Training;
import com.jacchm.project.repository.CustomTrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CustomTrainingRepositoryImpl implements CustomTrainingRepository {

  private static final String ID_FIELD = "id";
  private static final String USER_NAME = "userName";

  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  public Mono<Training> update(final String trainingId, final Training training, final String username) {

    return mongoTemplate.findAndModify(Query.query(Criteria.where(ID_FIELD).is(trainingId).and(USER_NAME).is(username)),
        Update.update(USER_NAME, training.getUsername()), Training.class);
  }
}
