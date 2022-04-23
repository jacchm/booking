package com.jacchm.project.controller;

import com.jacchm.project.controller.dto.TrainingCreationDTO;
import com.jacchm.project.controller.dto.TrainingDTO;
import com.jacchm.project.controller.dto.TrainingIdDTO;
import com.jacchm.project.controller.dto.TrainingUpdateDTO;
import com.jacchm.project.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("trainings")
public class TrainingController {

  private final TrainingService trainingService;

  @GetMapping
  public Mono<ResponseEntity<List<TrainingDTO>>> getTrainings(@RequestHeader final String username) {
    log.info("Retrieving training for user={}", username);
    return trainingService
        .findAllTrainings(username)
        .collectList()
        .map(ResponseEntity::ok);
  }

  @GetMapping("{id}")
  public Mono<ResponseEntity<TrainingDTO>> getTraining(@PathVariable("id") final String id,
                                                       @RequestHeader final String username) {
    log.info("Retrieving training with id={} for user={}", id, username);
    return trainingService
        .findTrainingById(id, username)
        .map(ResponseEntity::ok);
  }

  @PostMapping
  public Mono<ResponseEntity<TrainingIdDTO>> createTraining(@RequestBody final TrainingCreationDTO trainingCreationDTO,
                                                            @RequestHeader final String username) {
    log.info("Creating training for user={}", username);
    return trainingService
        .createTraining(trainingCreationDTO, username)
        .map(trainingIdDTO -> ResponseEntity
            .status(HttpStatus.CREATED)
            .body(trainingIdDTO));
  }

  @PutMapping("{id}")
  public Mono<ResponseEntity<Void>> updateTraining(@PathVariable("id") final String id,
                                                   @RequestBody final TrainingUpdateDTO trainingUpdateDto,
                                                   @RequestHeader final String username) {
    log.info("Updating training with id={}, for user={}, with values={}", id, username, trainingUpdateDto);
    return trainingService
        .updateTraining(id, trainingUpdateDto, username)
        .then(Mono.just(ResponseEntity.status(HttpStatus.OK).build()));
  }

  @DeleteMapping("{id}")
  public Mono<ResponseEntity<Void>> deleteTraining(@PathVariable("id") final String id,
                                                   @RequestHeader final String username) {
    log.info("Deleting training with id={} for user={}", id, username);
    return trainingService
        .deleteTraining(id, username)
        .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
  }

}
