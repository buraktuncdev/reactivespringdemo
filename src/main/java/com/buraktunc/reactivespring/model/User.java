package com.buraktunc.reactivespring.model;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nonapi.io.github.classgraph.json.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {
	@Id
	private Integer id;
	private String name;
	private int age;
	private double salary;
}

