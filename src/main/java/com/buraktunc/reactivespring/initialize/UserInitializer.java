package com.buraktunc.reactivespring.initialize;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.buraktunc.reactivespring.model.Department;
import com.buraktunc.reactivespring.model.User;
import com.buraktunc.reactivespring.repository.DepartmentRepository;
import com.buraktunc.reactivespring.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Profile("!test")
@Slf4j
public class UserInitializer implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Override
	public void run(String[] args){
		initialDataSetup();
	}

	private List<User> getData(){
		return Arrays.asList(new User(null,"Burak Tunc", 29, 111111),
				new User(null,"New User", 29, 123),
				new User(null,"GG Tunc", 29, 1551342),
				new User(null,"Batu Tunc", 23, 111111));
	}

	private List<Department> getDepartments(){
		return Arrays.asList(new Department(null,"Mechanical",1,"Turkey"),
				new Department(null,"Computer",2,"Germany"));
	}

	private void initialDataSetup(){
		userRepository.deleteAll()
				.thenMany(Flux.fromIterable(getData()))
				.flatMap(userRepository::save)
				.thenMany(userRepository.findAll())
				.subscribe(user -> {
					log.info("User Persisted While Initializing: " + user);
				});
		departmentRepository.deleteAll()
				.thenMany(Flux.fromIterable(getDepartments()))
				.flatMap(departmentRepository::save)
				.thenMany(departmentRepository.findAll())
				.subscribe(department -> {
					log.info("Department Persisted While Initializing: " + department);
				});
	}
}
