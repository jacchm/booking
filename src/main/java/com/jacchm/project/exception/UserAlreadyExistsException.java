package com.jacchm.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class UserAlreadyExistsException extends DomainException {

  private static final HttpStatus status = HttpStatus.CONFLICT;

  public UserAlreadyExistsException(final String message) {
    super(status, message);
  }

  public UserAlreadyExistsException(final String message, final List<String> errorDetails) {
    super(status, message, errorDetails);
  }

}
