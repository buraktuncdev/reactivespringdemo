package com.buraktunc.reactivespring.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.buraktunc.reactivespring.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

	@Query("select * from users where age >= $1")
	Flux<User> findByAge(int age);

	@Query("select * from users where id = $1")
	Mono<User> findById(int userId);

}