package com.jacchm.project.repository;

import com.jacchm.project.model.Training;
import reactor.core.publisher.Mono;

public interface CustomTrainingRepository {

  Mono<Training> update(final String trainingId, final Training training, final String username);

}
