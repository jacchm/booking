package com.jacchm.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RepositoryException extends DomainException {

  private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  public RepositoryException(String message) {
    super(status, message);
  }
}
