package com.buraktunc.reactivespring.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.buraktunc.reactivespring.model.Department;

import reactor.core.publisher.Mono;

public interface DepartmentRepository extends ReactiveCrudRepository<Department, Long> {
	Mono<Department> findByUserId(Integer userID);
}
