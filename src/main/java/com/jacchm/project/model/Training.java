package com.jacchm.project.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "trainings")
public class Training {

  @Id
  private String id;
  private int number;
  private Instant date;
  private Duration totalTime;
  private String username;
  private List<Exercise> exercises;

  @Version
  private Integer version;
  @CreatedDate
  private Instant createdAt;
  // TODO: fix - it doesn't work for update operation
  @LastModifiedDate
  private Instant modifiedAt;

}
