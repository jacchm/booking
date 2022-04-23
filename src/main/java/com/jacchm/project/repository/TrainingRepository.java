package com.jacchm.project.repository;

import com.jacchm.project.model.Training;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TrainingRepository extends ReactiveMongoRepository<Training, String>, CustomTrainingRepository {

  Mono<Training> findByIdAndUsername(final String id, final String username);

  Flux<Training> findAllByUsername(final String username);

  Mono<Void> deleteByIdAndUsername(final String id, final String username);

}
