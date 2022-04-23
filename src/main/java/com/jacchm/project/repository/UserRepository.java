package com.jacchm.project.repository;

import com.jacchm.project.model.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

  Mono<User> findByUsername(final String username);

  Mono<Void> deleteByUsername(final String username);

  Mono<User> findByEmail(final String email);

  @Modifying
  @Query("UPDATE users SET name = :name, surname = :surname, email = :email WHERE username = :username")
  Mono<Void> update(final String username, final String name, final String surname, final String email);

  Flux<User> findAll();

}
