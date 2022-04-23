package com.jacchm.project.service;

import com.jacchm.project.controller.dto.TrainingCreationDTO;
import com.jacchm.project.controller.dto.TrainingDTO;
import com.jacchm.project.controller.dto.TrainingIdDTO;
import com.jacchm.project.controller.dto.TrainingUpdateDTO;
import com.jacchm.project.exception.BadRequestException;
import com.jacchm.project.exception.NotFoundException;
import com.jacchm.project.exception.RepositoryException;
import com.jacchm.project.mapper.TrainingMapper;
import com.jacchm.project.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.jacchm.project.service.util.OperationType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingService {

  private static final String REPOSITORY_ERROR_MSG = """
      An error occurred while trying to connect to the database.""";
  private static final String TRAINING_NOT_FOUND_ERROR_MSG = """
      Training with id=%s has not been found.""";
  private static final String ANY_TRAINING_NOT_FOUND_ERROR_MSG = """
      Any training has not been found.""";
  private static final String TRAINING_ERROR_MSG = """
      Training with id={} has not been {}. Reason={}""";
  private static final String ANY_TRAINING_ERROR_MSG = """
      Any training has been {}. Reason={}""";
  private static final String TRAINING_NOT_CREATED_ERROR_MSG = """
      Training with has not been {}. Reason={}""";
  private static final String AMBIGUOUS_IDS_SPECIFIED_ERROR_MSG = """
      Ambiguous ids specified. Path parameter id=%s, body id=%s""";
  private static final String SUCCESSFUL_OPERATION_MSG = """
      Successfully {} training with id={}.""";
  private static final String MULTIPLE_SUCCESSFUL_OPERATION_MSG = """
      Successfully {} all trainings.""";

  private final TrainingRepository trainingRepository;
  private final TrainingMapper trainingMapper;

  public Flux<TrainingDTO> findAllTrainings(final String username) {
    return trainingRepository
        .findAllByUsername(username)
        .doOnError(error -> log.error(ANY_TRAINING_ERROR_MSG, FETCHED.getNameLowercase(), error.getMessage()))
        .onErrorMap(__ -> new RepositoryException(REPOSITORY_ERROR_MSG))
        .doOnNext(result -> log.info(MULTIPLE_SUCCESSFUL_OPERATION_MSG, FETCHED.getNameLowercase()))
        .switchIfEmpty(Mono.error(new NotFoundException(ANY_TRAINING_NOT_FOUND_ERROR_MSG)))
        .map(trainingMapper::mapTrainingToTrainingDTO);
  }

  public Mono<TrainingDTO> findTrainingById(final String id, final String username) {
    return trainingRepository
        .findByIdAndUsername(id, username)
        .doOnError(error -> log.error(TRAINING_ERROR_MSG, id, FETCHED.getNameLowercase(), error.getMessage()))
        .onErrorMap(__ -> new RepositoryException(REPOSITORY_ERROR_MSG))
        .doOnNext(result -> log.info(SUCCESSFUL_OPERATION_MSG, FETCHED.getNameLowercase(), id))
        .switchIfEmpty(Mono.error(new NotFoundException(String.format(TRAINING_NOT_FOUND_ERROR_MSG, id))))
        .map(trainingMapper::mapTrainingToTrainingDTO);
  }

  public Mono<TrainingIdDTO> createTraining(final TrainingCreationDTO trainingCreationDTO, final String username) {
    return Mono.just(trainingCreationDTO)
        .map(trainingDto -> setTrainingUser(trainingDto, username))
        .map(trainingMapper::mapTrainingCreationDTOToTraining)
        .flatMap(trainingRepository::save)
        .doOnError(error -> log.error(TRAINING_NOT_CREATED_ERROR_MSG, CREATED.getNameLowercase(), error.getMessage()))
        .onErrorMap(__ -> new RepositoryException(REPOSITORY_ERROR_MSG))
        .doOnNext(result -> log.info(SUCCESSFUL_OPERATION_MSG, CREATED.getNameLowercase(), result.getId()))
        .map(trainingMapper::mapTrainingToTrainingIdDTO);
  }

  public Mono<Void> updateTraining(final String id, final TrainingUpdateDTO trainingUpdate, final String username) {
    return Mono.just(trainingUpdate)
        .filter(trainingUpdateDTO -> isIdValid(id, trainingUpdate.getId()))
        .switchIfEmpty(Mono.error(new BadRequestException(
            String.format(AMBIGUOUS_IDS_SPECIFIED_ERROR_MSG, id, trainingUpdate.getId()))))
        .map(trainingMapper::mapTrainingUpdateDTOtoTraining)
        .flatMap(update -> trainingRepository.update(id, update, username)
            .onErrorMap(__ -> new RepositoryException(REPOSITORY_ERROR_MSG)))
        .doOnError(error -> log.error(TRAINING_ERROR_MSG, id, UPDATED.getNameLowercase(), error.getMessage()))
        .doOnNext(__ -> log.info(SUCCESSFUL_OPERATION_MSG, UPDATED.getNameLowercase(), id))
        .switchIfEmpty(Mono.error(new NotFoundException(String.format(TRAINING_NOT_FOUND_ERROR_MSG, id))))
        .then();
  }

  public Mono<Void> deleteTraining(final String id, final String username) {
    return trainingRepository.deleteByIdAndUsername(id, username)
        .doOnError(error -> log.error(TRAINING_ERROR_MSG, id, DELETED.getNameLowercase(), error.getMessage()))
        .onErrorMap(__ -> new RepositoryException(REPOSITORY_ERROR_MSG))
        .doOnSuccess(__ -> log.info(SUCCESSFUL_OPERATION_MSG, DELETED.getNameLowercase(), id))
        .then();
  }

  private TrainingCreationDTO setTrainingUser(final TrainingCreationDTO dto, final String username) {
    dto.setUsername(username);
    return dto;
  }

  private boolean isIdValid(final String id, final String bodyId) {
    return ObjectUtils.isEmpty(bodyId) || id.equals(bodyId);
  }
}
