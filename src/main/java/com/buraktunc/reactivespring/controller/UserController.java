package com.buraktunc.reactivespring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.buraktunc.reactivespring.dto.UserDepartmentDTO;
import com.buraktunc.reactivespring.model.User;
import com.buraktunc.reactivespring.service.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<User> createUser(@RequestBody User user){
		return userService.createUser(user);
	}

	@GetMapping
	public Flux<User> getAllUsers(){
		return userService.getAllUsers();
	}

	@GetMapping("/{userId}")
	public Mono<ResponseEntity<User>> getUserById(@PathVariable Integer userId){
		Mono<User> user = userService.findById(userId);
		return user.map(ResponseEntity::ok)
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PutMapping("/{userId}")
	public Mono<ResponseEntity<User>> updateUserById(@PathVariable Integer userId, @RequestBody User user){
		return userService.updateUser(userId, user)
				.map(updatedUser -> ResponseEntity.ok(updatedUser))
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	@DeleteMapping("/{userId}")
	public Mono<ResponseEntity<Void>> deleteByUserID(@PathVariable Integer userId){
		return userService.deleteUser(userId)
				.map(user -> ResponseEntity.ok().<Void>build())
				.defaultIfEmpty(ResponseEntity.badRequest().build());
	}

	@GetMapping("/age/{age}")
	public Flux<User> getUserByAge(@PathVariable int age){
		return userService.findUsersByAge(age);
	}

	@PostMapping("/search/id")
	public Flux<User> fetchUsersByIds(@RequestBody List<Integer> userIds){
		return userService.fetchUsers(userIds);
	}

	@GetMapping("/{userId}/department")
	public Mono<UserDepartmentDTO> fetchUserAndDepartment(@PathVariable Integer userId){
		return userService.fetchUserAndDepartment(userId);
	}








}
