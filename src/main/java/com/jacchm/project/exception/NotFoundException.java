package com.jacchm.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends DomainException {

  private static final HttpStatus status = HttpStatus.NOT_FOUND;

  public NotFoundException(String message) {
    super(status, message);
  }

}
