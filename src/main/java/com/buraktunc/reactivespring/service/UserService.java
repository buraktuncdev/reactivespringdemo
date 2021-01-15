package com.buraktunc.reactivespring.service;

import java.util.List;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.buraktunc.reactivespring.dto.UserDepartmentDTO;
import com.buraktunc.reactivespring.model.Department;
import com.buraktunc.reactivespring.model.User;
import com.buraktunc.reactivespring.repository.DepartmentRepository;
import com.buraktunc.reactivespring.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
@Transactional
public class UserService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentRepository departmentRepository;

	public Mono<User> createUser(User user){
		return userRepository.save(user);
	}

	public Flux<User> getAllUsers(){
		return userRepository.findAll();
	}

	public Mono<User> findById(Integer userId){
		return userRepository.findById(userId);
	}

	public Mono<User> updateUser(Integer userId, User user){
		return userRepository.findById(userId)
				.flatMap(dbUser -> {
					dbUser.setAge(user.getAge());
					dbUser.setSalary(user.getSalary());
					return userRepository.save(dbUser);
				});
	}

	public Mono<User> deleteUser(Integer userId){
		return userRepository.findById(userId)
				.flatMap(existingUser -> userRepository.delete(existingUser)
				.then(Mono.just(existingUser)));
	}

	public Flux<User> findUsersByAge(int age){
		return userRepository.findByAge(age);
	}

	public Flux<User> fetchUsers(List<Integer> userIds) {
		return Flux.fromIterable(userIds)
				.parallel()
				.runOn(Schedulers.elastic())
				.flatMap(this::findById)
				.ordered((u1,u2) -> u2.getId() - u1.getId());
	}

	private Mono<Department> getDepartmentByUserId(Integer userId){
		return departmentRepository.findByUserId(userId);
	}

	public Mono<UserDepartmentDTO> fetchUserAndDepartment(Integer userId){
		Mono<User> user = findById(userId).subscribeOn(Schedulers.elastic());
		Mono<Department> department = getDepartmentByUserId(userId).subscribeOn(Schedulers.elastic());
		return Mono.zip(user, department, userDepartMentDTOBiFunction);
	}

	private BiFunction<User, Department, UserDepartmentDTO> userDepartMentDTOBiFunction
			= (x1, x2) -> UserDepartmentDTO.builder()
			.age(x1.getAge())
			.departmentId(x2.getId())
			.departmentName(x2.getName())
			.userName(x1.getName())
			.loc(x2.getLoc())
			.salary(x1.getSalary()).build();
}
