package com.jacchm.project.service;

import com.jacchm.project.controller.dto.UserCreationDTO;
import com.jacchm.project.controller.dto.UserDTO;
import com.jacchm.project.controller.dto.UserUpdateDTO;
import com.jacchm.project.exception.RepositoryException;
import com.jacchm.project.exception.UserAlreadyExistsException;
import com.jacchm.project.mapper.UserMapper;
import com.jacchm.project.model.User;
import com.jacchm.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.jacchm.project.service.util.OperationType.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements ReactiveUserDetailsService {

  private static final String REPOSITORY_ERROR_MSG = """
      An error occurred while trying to connect to the database.""";
  private static final String SUCCESSFUL_OPERATION_MSG = """
      Successfully {} user with username={}.""";
  private static final String USER_NOT_FOUND_ERROR_MSG = """
      User with username=%s has not been found. Reason={}""";
  private static final String USER_ERROR_MSG = """
      User with username={} has not been {}. Reason={}""";

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public Mono<UserDetails> findByUsername(final String username) {
    return userRepository
        .findByUsername(username)
        .doOnError(error -> log.error(USER_NOT_FOUND_ERROR_MSG, error.getMessage()))
        .cast(UserDetails.class);
  }

  public Mono<List<UserDTO>> getAllUsers() {
    return userRepository
        .findAll()
        .doOnError(error -> log.error(USER_NOT_FOUND_ERROR_MSG, error.getMessage()))
        .map(userMapper::mapToUserDTO)
        .collectList();
  }

  public Mono<UserDTO> getUser(final String username) {
    return userRepository
        .findByUsername(username)
        .doOnError(error -> log.error(USER_NOT_FOUND_ERROR_MSG, error.getMessage()))
        .map(userMapper::mapToUserDTO);
  }

  public Mono<Void> createUser(final UserCreationDTO userCreationDTO) {
    return Mono.just(userCreationDTO)
        .map(userMapper::mapToUser)
        .map(this::encodeUserPasswordWithBCrypt)
        .flatMap(userRepository::save)
        .doOnError(error -> log.error(USER_ERROR_MSG, userCreationDTO.getUsername(),
            CREATED.getNameLowercase(), error.getMessage()))
        .onErrorMap(this::mapToProperException)
        .doOnNext(result -> log.info(SUCCESSFUL_OPERATION_MSG, CREATED.getNameLowercase(), result.getUsername()))
        .then();
  }

  public Mono<Void> updateUser(final String username, final UserUpdateDTO userUpdateDTO) {
    return userRepository
        .update(username, userUpdateDTO.getName(), userUpdateDTO.getSurname(), userUpdateDTO.getEmail())
        .doOnError(error -> log.error(USER_NOT_FOUND_ERROR_MSG, error.getMessage()))
        .doOnSuccess(__ -> log.info(SUCCESSFUL_OPERATION_MSG, UPDATED.getNameLowercase(), username));
  }

  public Mono<Void> deleteUser(final String username) {
    return userRepository
        .deleteByUsername(username)
        .doOnSuccess(__ -> log.info(SUCCESSFUL_OPERATION_MSG, DELETED.getNameLowercase(), username));
  }

  private User encodeUserPasswordWithBCrypt(final User user) {
    user.setPassword(String.format("{bcrypt}%s", passwordEncoder.encode(user.getPassword())));
    return user;
  }

  private Throwable mapToProperException(final Throwable error) {
    if (error instanceof DataIntegrityViolationException) {
      // TODO: make error details
      return new UserAlreadyExistsException(error.getMessage(), List.of("errorDetail1", "errorDetail2"));
    } else {
      return new RepositoryException(REPOSITORY_ERROR_MSG);
    }
  }
}
