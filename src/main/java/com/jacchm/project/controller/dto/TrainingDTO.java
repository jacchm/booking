package com.jacchm.project.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jacchm.project.model.Exercise;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
public class TrainingDTO {

  private String id;
  private int number;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant date;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Duration totalTime;
  private String username;
  private List<Exercise> exercises;
  private Integer version;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant createdAt;
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private Instant modifiedAt;
}
