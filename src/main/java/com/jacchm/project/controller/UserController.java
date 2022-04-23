package com.jacchm.project.controller;

import com.jacchm.project.controller.dto.UserCreationDTO;
import com.jacchm.project.controller.dto.UserDTO;
import com.jacchm.project.controller.dto.UserUpdateDTO;
import com.jacchm.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("users")
@RestController
public class UserController {

  private final UserService userService;

  @PostMapping
  public Mono<ResponseEntity<Void>> register(@RequestBody final UserCreationDTO userCreationDTO) {
    log.info("Received registration request with user data={}", userCreationDTO);
    return userService.createUser(userCreationDTO)
        .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
  }

  @GetMapping
  public Mono<ResponseEntity<List<UserDTO>>> getUsers() {
    return userService.getAllUsers()
        .map(ResponseEntity::ok);
  }

  @GetMapping("{username}")
  public Mono<ResponseEntity<UserDTO>> getUser(@PathVariable final String username) {
    return userService.getUser(username)
        .map(ResponseEntity::ok);
  }

  @PutMapping("{username}")
  public Mono<ResponseEntity<Void>> updateUser(@PathVariable final String username,
                                               @RequestBody final UserUpdateDTO userUpdateDTO) {
    return userService.updateUser(username, userUpdateDTO)
        .then(Mono.just(ResponseEntity.status(HttpStatus.OK).build()));
  }

  @DeleteMapping("{username}")
  public Mono<ResponseEntity<Void>> deleteUser(@PathVariable final String username) {
    log.info("Received delete request with username={}", username);
    return userService.deleteUser(username)
        .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build()));
  }

}
